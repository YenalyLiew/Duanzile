@file:Suppress("unused")

package com.yenaly.yenaly_libs.utils

import android.app.Activity
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 *  快捷启动Activity
 *
 *  @param Ava    泛型，继承于Activity
 *  @param values 需要传过去的值
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 */
inline fun <reified Ava : Activity> Activity.startActivity(
    vararg values: Pair<String, Any>,
    flag: Int? = null,
    extra: Bundle? = null
) = startActivity(getIntent<Ava>(flag, extra, *values))

/**
 *  快捷启动Activity
 *
 *  @param Ava    泛型，继承于Activity
 *  @param flag   flag (optional)
 */
inline fun <reified Ava : Activity> Activity.startActivity(
    flag: Int? = null,
    extra: Bundle? = null
) = Intent(this, Ava::class.java).apply {
    flag?.let { flags = it }
    extra?.let { putExtras(it) }
    startActivity(this)
}

/**
 *  快捷启动Service
 *
 *  @param S      泛型，继承于Service
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 *  @param values 需要传过去的值
 */
inline fun <reified S : Service> Activity.startService(
    vararg values: Pair<String, Any>,
    flag: Int? = null,
    extra: Bundle? = null
) = startService(getIntent<S>(flag, extra, *values))

/**
 *  快捷启动Service
 *
 *  @param S      泛型，继承于Service
 *  @param flag   flag (optional)
 */
inline fun <reified S : Service> Activity.startService(
    flag: Int? = null,
    extra: Bundle? = null
) = Intent(this, S::class.java).apply {
    flag?.let { flags = it }
    extra?.let { putExtras(it) }
    startService(this)
}

/**
 *  快捷启动Activity
 *
 *  @param Bella  泛型，继承于Activity
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 *  @param values 需要传过去的值
 */
inline fun <reified Bella : Activity> Fragment.startActivity(
    flag: Int? = null,
    extra: Bundle? = null,
    vararg values: Pair<String, Any>
) = activity?.let {
    startActivity(it.getIntent<Bella>(flag, extra, *values))
}

/**
 *  快捷启动Activity
 *
 *  @param Bella  泛型，继承于Activity
 *  @param flag   flag (optional)
 */
inline fun <reified Bella : Activity> Fragment.startActivity(
    flag: Int? = null,
    extra: Bundle? = null
) = activity?.let { activity ->
    Intent(activity, Bella::class.java).apply {
        flag?.let { flags = it }
        extra?.let { putExtras(it) }
        startActivity(this)
    }
}

/**
 *  快捷启动Service
 *
 *  @param S      泛型，继承于Service
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 *  @param values 需要传过去的值
 */
inline fun <reified S : Service> Fragment.startService(
    vararg values: Pair<String, Any>,
    flag: Int? = null,
    extra: Bundle? = null
) = activity?.let {
    it.startService(it.getIntent<S>(flag, extra, *values))
}

/**
 *  快捷启动Service
 *
 *  @param S      泛型，继承于Service
 *  @param flag   flag (optional)
 */
inline fun <reified S : Service> Fragment.startService(
    flag: Int? = null,
    extra: Bundle? = null
) = activity?.let { activity ->
    Intent(activity, S::class.java).apply {
        flag?.let { flags = it }
        extra?.let { putExtras(it) }
        activity.startService(this)
    }
}

/**
 *  快捷启动Activity
 *
 *  @param Carol  泛型，继承于Activity
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 *  @param values 需要传过去的值
 */
inline fun <reified Carol : Activity> Context.startActivity(
    flag: Int? = null,
    extra: Bundle? = null,
    vararg values: Pair<String, Any>
) = startActivity(getIntent<Carol>(flag, extra, *values))

/**
 *  快捷启动Activity
 *
 *  @param Carol  泛型，继承于Activity
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 */
inline fun <reified Carol : Activity> Context.startActivity(
    flag: Int? = null,
    extra: Bundle? = null
) = Intent(this, Carol::class.java).apply {
    flag?.let { flags = it }
    extra?.let { putExtras(it) }
    startActivity(this)
}

/**
 *  快捷启动Service
 *
 *  @param S      泛型，继承于Service
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 *  @param values 需要传过去的值
 */
inline fun <reified S : Service> Context.startService(
    flag: Int? = null,
    extra: Bundle? = null,
    vararg values: Pair<String, Any>
) = startService(getIntent<S>(flag, extra, *values))

/**
 *  快捷启动Service
 *
 *  @param S      泛型，继承于Service
 *  @param flag   flag (optional)
 *  @param extra  附带的bundle (optional)
 */
inline fun <reified S : Service> Context.startService(
    flag: Int? = null,
    extra: Bundle? = null
) = Intent(this, S::class.java).apply {
    flag?.let { flags = it }
    extra?.let { putExtras(it) }
    startService(this)
}

/**
 * 快捷获取一个携带各种参数的intent
 *
 * @param Diana  泛型，继承于Activity
 * @param flag   flag (optional)
 * @param extra  附带的bundle (optional)
 * @param pairs  需要传过去的值
 */
inline fun <reified Diana : Context> Context.getIntent(
    flag: Int? = null,
    extra: Bundle? = null,
    vararg pairs: Pair<String, Any>
): Intent = Intent(this, Diana::class.java).apply {
    flag?.let { flags = it }
    extra?.let { putExtras(it) }
    pairs.forEach { pair ->
        val name = pair.first
        when (val value = pair.second) {
            is Int -> putExtra(name, value)
            is Byte -> putExtra(name, value)
            is Char -> putExtra(name, value)
            is Short -> putExtra(name, value)
            is Boolean -> putExtra(name, value)
            is Long -> putExtra(name, value)
            is Float -> putExtra(name, value)
            is Double -> putExtra(name, value)
            is String -> putExtra(name, value)
            is CharSequence -> putExtra(name, value)
            is Parcelable -> putExtra(name, value)
            is Array<*> -> putExtra(name, value)
            is ArrayList<*> -> putExtra(name, value)
            is Serializable -> putExtra(name, value)
            is BooleanArray -> putExtra(name, value)
            is ByteArray -> putExtra(name, value)
            is ShortArray -> putExtra(name, value)
            is CharArray -> putExtra(name, value)
            is IntArray -> putExtra(name, value)
            is LongArray -> putExtra(name, value)
            is FloatArray -> putExtra(name, value)
            is DoubleArray -> putExtra(name, value)
            is Bundle -> putExtra(name, value)
            is Intent -> putExtra(name, value)
            else -> throw IllegalArgumentException("Intent extra ${pair.first} has wrong type ${value.javaClass.name}")
        }
    }
}

/**
 * 用委托方式获取activity传来的extra，
 * 基本类型涉及装箱拆箱
 *
 * @param Eileen 泛型
 * @param name 传值的名字
 */
@Suppress("UNCHECKED_CAST")
fun <Eileen> Activity.intentExtra(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    intent.extras?.get(name) as? Eileen
}

/**
 * 用委托方式获取activity传来的extra
 *
 * @param Eileen 泛型
 * @param name 传值的名字
 * @param default 缺省值
 */
@Suppress("UNCHECKED_CAST")
fun <Eileen> Activity.intentExtra(name: String, default: Eileen) = lazy(LazyThreadSafetyMode.NONE) {
    intent.extras?.get(name) as? Eileen ?: default
}

/**
 * 用委托方式获取activity传来的extra，
 * 若空则直接报错
 *
 * @param Eileen 泛型
 * @param name 传值的名字
 */
@Suppress("UNCHECKED_CAST")
fun <Eileen> Activity.safeIntentExtra(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    val extra = intent.extras?.get(name) as? Eileen
    checkNotNull(extra) { "No intent value for key \"$name\"" }
}

/**
 * 用委托方式获取fragment所属activity传来的extra，
 * 基本类型涉及装箱拆箱
 *
 * @param Yoyi 泛型
 * @param name 传值的名字
 */
@Suppress("UNCHECKED_CAST")
fun <Yoyi> Fragment.activityIntentExtra(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    activity?.intent?.extras?.get(name) as? Yoyi
}

/**
 * 用委托方式获取fragment所属activity传来的extra
 *
 * @param Yoyi 泛型
 * @param name 传值的名字
 * @param default 缺省值
 */
@Suppress("UNCHECKED_CAST")
fun <Yoyi> Fragment.activityIntentExtra(name: String, default: Yoyi) =
    lazy(LazyThreadSafetyMode.NONE) {
        activity?.intent?.extras?.get(name) as? Yoyi ?: default
    }

/**
 * 用委托方式获取fragment所属activity传来的extra，
 * 若空则直接报错
 *
 * @param Yoyi 泛型
 * @param name 传值的名字
 */
@Suppress("UNCHECKED_CAST")
fun <Yoyi> Fragment.safeActivityIntentExtra(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    val extra = activity?.intent?.extras?.get(name) as? Yoyi
    checkNotNull(extra) { "No intent value for key \"$name\"" }
}

/**
 * 用委托方式接收arguments，
 * 基本类型涉及装箱拆箱
 *
 * @param Bekki 泛型
 * @param name  传值的名字
 */
@Suppress("UNCHECKED_CAST")
fun <Bekki> Fragment.arguments(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    arguments?.get(name) as? Bekki
}

/**
 * 用委托方式接收arguments
 *
 * @param Bekki   泛型
 * @param name    传值的名字
 * @param default 缺省值
 */
@Suppress("UNCHECKED_CAST")
fun <Bekki> Fragment.arguments(name: String, default: Bekki) = lazy(LazyThreadSafetyMode.NONE) {
    arguments?.get(name) as? Bekki ?: default
}

/**
 * 用委托方式接收arguments，
 * 若空则直接报错
 *
 * @param Bekki   泛型
 * @param name    传值的名字
 */
@Suppress("UNCHECKED_CAST")
fun <Bekki> Fragment.safeArguments(name: String) = lazy(LazyThreadSafetyMode.NONE) {
    val argument = arguments?.get(name) as? Bekki
    checkNotNull(argument) { "No argument value for key \"$name\"" }
}

/**
 * 通过uri浏览
 *
 * @param uri uri地址
 */
infix fun Activity.browse(uri: String) {
    val mUri = Uri.parse(uri)
    val intent = Intent(Intent.ACTION_VIEW, mUri)
    startActivity(intent)
}

/**
 * 通过uri浏览
 *
 * @param uri uri地址
 */
infix fun Fragment.browse(uri: String) {
    val mUri = Uri.parse(uri)
    val intent = Intent(Intent.ACTION_VIEW, mUri)
    startActivity(intent)
}

/**
 * 快捷给Fragment传值
 *
 * @param params 需要传过去的值
 *
 * @return 带值的本身
 */
fun Fragment.makeBundle(vararg params: Pair<String, Any>): Fragment {
    return this.apply {
        arguments = Bundle().apply {
            makeParams(*params)
        }
    }
}

/**
 * 给bundle放参数
 *
 * @param pairs 需要传的值
 */
fun Bundle.makeParams(vararg pairs: Pair<String, Any>) {
    pairs.forEach { pair ->
        when (val value = pair.second) {
            is Int -> putInt(pair.first, value)
            is Long -> putLong(pair.first, value)
            is CharSequence -> putCharSequence(pair.first, value)
            is String -> putString(pair.first, value)
            is Float -> putFloat(pair.first, value)
            is Double -> putDouble(pair.first, value)
            is Char -> putChar(pair.first, value)
            is Short -> putShort(pair.first, value)
            is Boolean -> putBoolean(pair.first, value)
            is Serializable -> putSerializable(pair.first, value)
            is Bundle -> putBundle(pair.first, value)
            is Parcelable -> putParcelable(pair.first, value)
            is IntArray -> putIntArray(pair.first, value)
            is LongArray -> putLongArray(pair.first, value)
            is FloatArray -> putFloatArray(pair.first, value)
            is DoubleArray -> putDoubleArray(pair.first, value)
            is CharArray -> putCharArray(pair.first, value)
            is ShortArray -> putShortArray(pair.first, value)
            is BooleanArray -> putBooleanArray(pair.first, value)
            else -> throw IllegalArgumentException("Intent extra ${pair.first} has wrong type ${value.javaClass.name}")
        }
    }
}

/**
 * Visit app in app store
 *
 * @param packageName default value is current app
 */
fun Context.openInAppStore(packageName: String = this.packageName) {
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        intent.data = Uri.parse("market://details?id=$packageName")
        startActivity(intent)
    } catch (ifPlayStoreNotInstalled: ActivityNotFoundException) {
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        startActivity(intent)
    }
}

/**
 * Open app by [packageName]
 */
fun Context.openApp(packageName: String) =
    packageManager.getLaunchIntentForPackage(packageName)?.run { startActivity(this) }

/**
 * Send email
 *
 * @param email the email address be sent to
 * @param subject a constant string holding the desired subject line of a message, @see [Intent.EXTRA_SUBJECT]
 * @param text a constant CharSequence that is associated with the Intent, @see [Intent.EXTRA_TEXT]
 */
fun Context.sendEmail(email: String, subject: String?, text: String?) {
    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).run {
        subject?.let { putExtra(Intent.EXTRA_SUBJECT, subject) }
        text?.let { putExtra(Intent.EXTRA_TEXT, text) }
        startActivity(this)
    }
}

/**
 * Return the Intent with [Settings.ACTION_APPLICATION_DETAILS_SETTINGS]
 */
fun Context.getAppInfoIntent(packageName: String = this.packageName): Intent =
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

/**
 * Jump to the app info page
 */
fun Context.goToAppInfoPage(packageName: String = this.packageName) {
    startActivity(getAppInfoIntent(packageName))
}