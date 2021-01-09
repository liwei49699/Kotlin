package com.liwei.kotlin.snapshot.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * # ***********************************************************************************************
 * # ClassName:      ActivityHandler.java
 * # Function:       全局Handler，注意不要使用handler.removeCallbacksAndMessages(0)，因为我们的Handler是单例的
 * # @author         ylrong
 * # @version        Ver 1.0
 * # ***********************************************************************************************
 * # Modified By     ylrong     2014-9-15 下午5:30:38
 * # Modifications:  initial
 * # ***********************************************************************************************
 */
public class ActivityHandler {
    /**
     * 回调
     */
    private static WeakReference<ActivityHandlerListener> activityHandlerListener;

    /****************************静态内部类实现单例***********************************/
    private static class ActivityHandlerInstance {
        private static ActivityHandler instance = new ActivityHandler();
    }

    private ActivityHandler() {

    }

    public static ActivityHandler getInstance(ActivityHandlerListener listener) {
        activityHandlerListener = new WeakReference<>(listener);
        return ActivityHandlerInstance.instance;
    }

    /**
     * 默认id,不建议使用，增加此是为了兼容旧框架版本
     */
    @Deprecated
    public static final int DEFAULT_WHAT = 10000;
    /**
     * 横竖屏专用（OrientationBaseActivity）
     */
    public static final int ORIENTATION_USER_ID = 10001;
    public static final int ORIENTATION_PORTRAIT_ID = 10002;

    private Lock mLock = new ReentrantLock();
    private final ChainedRef mRunnables = new ChainedRef(mLock, null);



    public OnlyHandler getHandler() {
        return handler;
    }

    private OnlyHandler handler = new OnlyHandler(this);

    public boolean sendEmptyMessage(int what) {
        return sendEmptyMessageDelayed(what, 0);
    }

    /**
     * 不建议使用，增加此是为了兼容旧框架版本
     *
     * @param obj obj
     * @return boolean
     * @deprecated use {@link #sendMessage(int, Object)} instead
     */
    @Deprecated
    public boolean sendEmptyMessage(Object obj) {
        return sendMessage(DEFAULT_WHAT, obj);
    }

    public boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }

    public boolean sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        return sendMessage(msg);
    }

    public boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    public boolean sendMessageDelayed(Message msg, long delayMillis) {
        msg.obj = new MessageModel(activityHandlerListener, msg.obj);
        return handler.sendMessageDelayed(msg, delayMillis);
    }

    public boolean hasMessages(int what) {
        return handler.hasMessages(what);
    }

    public void removeMessages(int what) {
        handler.removeMessages(what);
    }

    public boolean post(Runnable r) {
        return postDelayed(wrapRunnable(r), 0);
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        Message msg = Message.obtain(handler, r);
        return sendMessageDelayed(msg, delayMillis);
    }

    public Message obtainMessage(int msgId) {
        return handler.obtainMessage(msgId);
    }

    /**
     * Subclasses must implement this to receive messages.
     */
    private void handleMessage(Message msg) {
        MessageModel model = (MessageModel) msg.obj;
        msg.obj = model.obj;
        if (model.listener != null) {
            ActivityHandlerListener listener = model.listener.get();
            if (listener != null) {
                listener.handleMessage(msg);
            }
        }
    }

    static class MessageModel {
        public WeakReference<ActivityHandlerListener> listener;
        public Object obj;

        MessageModel(WeakReference<ActivityHandlerListener> listener, Object obj) {
            this.listener = listener;
            this.obj = obj;
        }
    }

    public interface ActivityHandlerListener {
        /**
         * 消息处理
         *
         * @param msg 消息体
         */
        void handleMessage(Message msg);
    }

    static class OnlyHandler extends Handler {

        private WeakReference<ActivityHandler> wr;

        OnlyHandler(ActivityHandler ahl) {
            super(Looper.getMainLooper());
            wr = new WeakReference<>(ahl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ActivityHandler ahl = wr.get();
            try {
                ahl.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mDelegate;
        private final WeakReference<ChainedRef> mReference;

        WeakRunnable(WeakReference<Runnable> delegate, WeakReference<ChainedRef> reference) {
            mDelegate = delegate;
            mReference = reference;
        }

        @Override
        public void run() {
            final Runnable delegate = mDelegate.get();
            final ChainedRef reference = mReference.get();
            if (reference != null) {
                reference.remove();
            }
            if (delegate != null) {
                delegate.run();
            }
        }
    }

    private WeakRunnable wrapRunnable(Runnable r) {
        if (r == null) {
            throw new NullPointerException("Runnable can't be null");
        }
        final ChainedRef hardRef = new ChainedRef(mLock, r);
        mRunnables.insertAfter(hardRef);
        return hardRef.wrapper;
    }

    static class ChainedRef {
        ChainedRef next;
        ChainedRef prev;
        final Runnable runnable;
        final WeakRunnable wrapper;
        Lock lock;

        ChainedRef(Lock lock, Runnable r) {
            this.runnable = r;
            this.lock = lock;
            this.wrapper = new WeakRunnable(new WeakReference<>(r), new WeakReference<>(this));
        }

        public WeakRunnable remove() {
            lock.lock();
            try {
                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }
                prev = null;
                next = null;
            } finally {
                lock.unlock();
            }
            return wrapper;
        }

        void insertAfter(ChainedRef candidate) {
            lock.lock();
            try {
                if (this.next != null) {
                    this.next.prev = candidate;
                }

                candidate.next = this.next;
                this.next = candidate;
                candidate.prev = this;
            } finally {
                lock.unlock();
            }
        }

        public WeakRunnable remove(Runnable obj) {
            lock.lock();
            try {
                ChainedRef curr = this.next; // Skipping head
                while (curr != null) {
                    if (curr.runnable == obj) { // We do comparison exactly how Handler does inside
                        return curr.remove();
                    }
                    curr = curr.next;
                }
            } finally {
                lock.unlock();
            }
            return null;
        }
    }
}
