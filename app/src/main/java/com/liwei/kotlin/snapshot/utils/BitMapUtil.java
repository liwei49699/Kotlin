package com.liwei.kotlin.snapshot.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.liwei.kotlin.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


/**
 * Bitmap工具类,缓存用过的指定数量的图片,使用此工具类,不再需要手动管理Bitmap内存 原理:
 * 用一个队列保存使用Bitmap的顺序,每次使用Bitmap将对象移动到队列头 当内存不够,或者达到制定的缓存数量的时候,回收队列尾部图片
 * 保证当前使用最多的图片得到最长时间的缓存,提高速度
 */
public final class BitMapUtil {

    private static final Size ZERO_SIZE = new Size(0, 0);
    private static final Options OPTIONS_GET_SIZE = new Options();
    private static final byte[] LOCKED = new byte[0];

    // 此对象用来保持Bitmap的回收顺序,保证最后使用的图片被回收  
    private static final LinkedList<String> CACHE_ENTRIES = new LinkedList<String>();

    // 线程请求创建图片的队列  
    private static final Queue<QueueEntry> TASK_QUEUE = new LinkedList<QueueEntry>();

    // 保存队列中正在处理的图片的key,有效防止重复添加到请求创建队列  
    private static final Set<String> TASK_QUEUE_INDEX = new HashSet<String>();

    //通过图片路径,图片大小   缓存Bitmap   
    private static final Map<String, Bitmap> IMG_CACHE_INDEX = new HashMap<String, Bitmap>();

    private static int CACHE_SIZE = 200; // 缓存图片数量  
    //    private static String filePath = null;

    private static final String FILEPATH = BaseApplication.getInstance().getApplicationContext().
            getFilesDir().getPath() + "/skin/";

    static {
        OPTIONS_GET_SIZE.inJustDecodeBounds = true;
        // 初始化创建图片线程,并等待处理  
        new Thread() {
            {
                setDaemon(true);
            }

            @Override
            public void run() {
                while (true) {
                    synchronized (TASK_QUEUE) {
                        if (TASK_QUEUE.isEmpty()) {
                            try {
                                TASK_QUEUE.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    QueueEntry entry = TASK_QUEUE.poll();
                    TASK_QUEUE_INDEX.remove(entry.fileName);
                    //createBitmap(entry.path, entry.width, entry.height);  
                    //修正过的代码  
                    getBitmap(entry.fileName);
                }
            }
        }.start();

    }

    /**
     * 创建一张图片 如果缓存中已经存在,则返回缓存中的图,否则创建一个新的对象,并加入缓存
     *
     * @param fileName 图片名
     * @return
     */
    public static Bitmap getBitmap(String fileName) {
        Bitmap bitMap = null;
        try {
            if (CACHE_ENTRIES.size() >= CACHE_SIZE) {
                destoryLast();
            }
            bitMap = useBitmap(fileName);
            if (bitMap != null && !bitMap.isRecycled()) {
                return bitMap;
            }
            bitMap = createBitmap(fileName);
            if (bitMap != null) {
                synchronized (LOCKED) {
                    IMG_CACHE_INDEX.put(fileName, bitMap);
                    CACHE_ENTRIES.addFirst(fileName);
                }
            }
        } catch (OutOfMemoryError err) {
            destoryLast();
            return getBitmap(fileName);
        }
        return bitMap;
    }

    /**
     * 设置缓存图片数量 如果输入负数,会产生异常
     *
     * @param size
     */
    public static void setCacheSize(int size) {
        if (size <= 0) {
            throw new RuntimeException("size :" + size);
        }
        while (size < CACHE_ENTRIES.size()) {
            destoryLast();
        }
        CACHE_SIZE = size;
    }

    /**
     * 加入一个图片处理请求到图片创建队列 
     *
     * @param path
     *            图片路径(本地) 
     * @param width
     *            图片宽度 
     * @param height
     *            图片高度 
     */
    //    public static void addTask(String fileName, int width, int height) {
    //        QueueEntry entry = new QueueEntry();
    //        entry.fileName = fileName;
    //        entry.width = width;
    //        entry.height = height;
    //        synchronized (TASK_QUEUE) {
    //            if (!TASK_QUEUE_INDEX.contains(fileName)
    //                    && !IMG_CACHE_INDEX.containsKey(fileName)) {
    //                TASK_QUEUE.add(entry);
    //                TASK_QUEUE_INDEX.add(fileName);
    //                TASK_QUEUE.notify();
    //            }
    //        }
    //    }

    /**
     * 通过图片路径返回图片实际大小
     *
     * @param fileName 图片物理路径
     * @return
     */
    private static Size getBitMapSize(String fileName) {
        InputStream in = null;
        try {
            in = BaseApplication.getInstance().getAssets().open(fileName);

            if (in != null) {
                BitmapFactory.decodeStream(in, null, OPTIONS_GET_SIZE);
                return new Size(OPTIONS_GET_SIZE.outWidth,
                        OPTIONS_GET_SIZE.outHeight);
            }
        } catch (FileNotFoundException e) {
            return ZERO_SIZE;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                closeInputStream(in);
            }
        }
        return ZERO_SIZE;
    }

    // ------------------------------------------------------------------ private Methods  
    // 将图片加入队列头  
    private static Bitmap useBitmap(String fileName) {
        Bitmap bitMap = null;
        synchronized (LOCKED) {
            bitMap = IMG_CACHE_INDEX.get(fileName);
            if (null != bitMap) {
                if (CACHE_ENTRIES.remove(fileName)) {
                    CACHE_ENTRIES.addFirst(fileName);
                }
            }
        }
        return bitMap;
    }

    // 回收最后一张图片  
    private static void destoryLast() {
        synchronized (LOCKED) {
            String key = CACHE_ENTRIES.removeLast();
            if (key.length() > 0) {
                Bitmap bitMap = IMG_CACHE_INDEX.remove(key);
                if (bitMap != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                    bitMap = null;
                }
            }
        }
    }

    //销毁所有的图片
    public static void reAddAll() {
        synchronized (LOCKED) {
            int size = CACHE_ENTRIES.size();
            for (int i = 0; i < size; i++) {
                String fileName = CACHE_ENTRIES.get(i);
                Bitmap bitMap = createBitmap(fileName);
                synchronized (LOCKED) {
                    IMG_CACHE_INDEX.put(fileName, bitMap);
                }
            }
        }
    }

    // 通过图片路径,宽度高度创建一个Bitmap对象  
    private static Bitmap createBitmap(String fileName) {
        if (fileName == null) {
            return null;
        }
        InputStream in = null;
        try {
            in = BaseApplication.getInstance().getAssets().open(fileName);
            if (in != null) {
                Size size = getBitMapSize(fileName);
                if (size.equals(ZERO_SIZE)) {
                    return null;
                }
                Bitmap bitMap = BitmapFactory.decodeStream(in);
                return bitMap;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                closeInputStream(in);
            }
        }
        return null;
    }

    // 关闭输入流  
    private static void closeInputStream(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 图片大小  
    static class Size {
        private int width, height;

        Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    // 队列缓存参数对象  
    static class QueueEntry {
        public String fileName;
        public int width;
        public int height;
    }

    /**
     * 保存图片到sd卡
     *
     * @param bitmap
     * @param dirPath
     * @param fileName
     * @param quality
     */
    public static void saveBitmap(Bitmap bitmap, String dirPath, String fileName, int quality) {
        FileOutputStream out = null;
        try {
            File path = new File(dirPath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(dirPath, fileName);
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存图片到sd卡
     *
     * @param bitmap
     * @param strFileName
     * @return
     */
    public static boolean savePic(Bitmap bitmap, String strFileName) {
        if (null == bitmap || null == strFileName) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            File saveFile = new File(strFileName);
            if (!saveFile.exists()) {
                File path = saveFile.getParentFile();
                if (!path.exists()) {
                    path.mkdirs();
                }
            } else {
                saveFile.delete();
            }
            fos = new FileOutputStream(strFileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}  
