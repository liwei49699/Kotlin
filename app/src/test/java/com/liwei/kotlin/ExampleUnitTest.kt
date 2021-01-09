package com.liwei.kotlin

import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {

        val c = C("ta")
        c.a(B())
        c.a(A())

        val d = D("tata")
        d.a(B())
        d.a(A())

        val f = 10
        val i = f to 19
        println(i)

    }

    @Test
    fun heightFun() {
        val runnable = Runnable {
            println("开启一个runnable")
        }

        Thread(runnable).apply {
            this.isDaemon = true
            start()
        }


        val let = 99.let {
            it + 10
            it + 11
        }
        println(let)

        val with = with("abc") {
            val plus = this.plus(1)
            println(plus)
            plus.length
        }

        println(with)

        val run = "abc".run {
            this.plus(1)
        }
        println(run)

        val e = E()
        println("构造器")
        e.c.value

        val newInputStream = Files.newInputStream(Paths.get("/Users/programer/Desktop/Kotlin/app/src/test/java/com/liwei/kotlin/ExampleUnitTest.kt"))
        val use = newInputStream.use {
            it.read()
            it.reset()
            1
        }

        print(use)

        List(1000){

        }
    }
}

class E {

    var a: Int? = null
    lateinit var  b:String
    val c = lazy{
        println("asdasd")
        "1"
    }
}

open class A()
class B(): A()

open class C(private val name: String) {

    open fun A.a(name: String){
        println("$name 111")
    }

    open fun B.a(name: String) {
        println("$name 2222")
    }

    fun a(aa: A) {
        aa.a(name)
    }


}

open class D(val name: String): C(name) {
    override fun A.a(name: String) {
        println("$name 33333")
    }

    override fun B.a(name: String) {
        println("$name 44444")
    }

    fun a(aa: B) {
        aa.a(name)
    }
}

infix fun Int.to(sun: Int): Int{
    return sun - this
}

