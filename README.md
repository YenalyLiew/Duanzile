# Duanzile

段子乐的第三方**考核用**APP，不能替代正统段子乐APP。

## 简介

段子乐第三方**考核用**APP，项目本身全部采用 Kotlin 编写，标准使用 MVVM 架构，Material 3 最新视觉标准，适配 Android 12 动态颜色模式，最低支持 Android 5（API 21）。

## 使用库

1. `Flow`（切换线程，网络数据分发，ViewModel 缓存）
2. `Retrofit`（网络请求）
3. `Jetpack`（基本界面）
4. `Glide`（展示图像）
5. `Jzvd`（展示视频）
6. `PhotoView`（展示图像）
7. `Paging`（分页加载）
8. `SwipeRefreshLayout`（滑动刷新布局）

## 截图

- 主页一览

  ![主页面](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-02-18-850_com.yenaly.dua.jpg)

  ![划一划](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-02-24-071_com.yenaly.dua.jpg)

  ![消息](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-02-28-933_com.yenaly.dua.jpg)

  ![我的](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-02-32-649_com.yenaly.dua.jpg)

- 搜索界面一览

  ![搜索](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-02-40-490_com.yenaly.dua.jpg)

- 个人中心一览

  ![个人中心](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-02-49-610_com.yenaly.dua.jpg)

  ![个人中心帖子](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-03-22-202_com.yenaly.dua.jpg)

  ![关注与粉丝](https://github.com/YenalyLiew/Duanzile/blob/master/DuanzileScreenshot/Screenshot_2022-07-24-22-03-33-190_com.yenaly.dua.jpg)

## 亮点

### BottomNavigationViewMediator

使用了自己封装的`BottomNavigationViewMediator`，极大简化了底部导航栏与 ViewPager2 的结合过程，类似于轻量版的 Navigation Jetpack。与`TabLayoutMediator`类似，只需要几行代码就能完成之间的结合，如**本项目**中的代码：

```kotlin
// MainActivity.kt

val bnvMediator = BottomNavigationViewMediator(
    binding.bnvMain, binding.vpMain, listOf(
        R.id.nav_home to HomeFragment(),
        R.id.nav_slide_video to SlideVideoFragment(),
        R.id.nav_message to MessageFragment(),
        R.id.nav_personal to PersonalFragment()
    )
).attach()
```

只需要传入 BottomNavigationView 与 ViewPager2 的实例和 menu id 与 Fragment 的 Pair 的 List，就能轻松结合。

通过该种方式，还能获取到每次切换到的 Fragment，更加轻松的管理 Fragment，如**本项目**中的代码：

```kotlin
// MainActivity.kt

bnvMediator.setOnFragmentChangedListener { currentFragment ->
    when (currentFragment) {
        is HomeFragment -> binding.bnvMain.toggleBottomNavBehavior(binding.vpMain, true)
        else -> binding.bnvMain.toggleBottomNavBehavior(binding.vpMain, false)
    }
    // ...
}
```

`BottomNavigationView.toggleBottomNavBehavior(View, Boolean)`解决了有时给底部导航栏的`layout_behavior`赋值 上下滑动时隐藏 的 behavior 时无效的问题，并且通过微调该 Behavior 的部分源码，修复了从一个隐藏底部导航栏的 Fragment 滑动切换到另一个不需要该 behavior 的 Fragment 时，底部导航栏不重新显示的问题。

### SingleFlowLaunch

使用了自己封装的`SingleFlowLaunch`，可以通过`viewModelScope.singleLaunch(Any, ...)`来进行ViewModelScope 的单次 launch。

为何要封装单次使用的函数？我们经常会这样使用 Flow：

```kotlin
// ViewModel
class XXXViewModel : ViewModel() {
    private val _flow = MutableSharedFlow()
    val flow = _flow.asSharedFlow()
    
    fun doSomething() {
        viewModelScope.launch {
            Repo.suspendFunction().collect(_flow::emit)
        }
    }
}

// Fragment
class XXXFragment : Fragment() {
    override fun onViewCreated(b: Bundle) {
        super.onViewCreated(b)
        viewModel.doSomething()
        viewLifecycleOwner.lifecycleScope.launch {
            // collect ...
        }
    }
}
```

这样有个问题，只要每次 Fragment 重建，`doSomething()`就会再调用一次，这样 ViewModelScope 毫无其用武之地。你说这不简单，放进 ViewModel 的`init`块里不就行了。我说我要加参数呢，你说这不简单，我给 ViewModel 传参数整个 Factory 不就得了。我只能说简单的页面这样还行，如果复杂起来，比如满足某个条件再触发`doSomething()`，而且是单次请求，而且带参数，会非常麻烦。

所以图方便，我自己整了个`SingleFlowLaunch`，通过给每个请求加 tag，并储存在 Map 中，利用类似 LiveData 的版本管理方式，控制该 launch 单次进行。并且在自己的 YenalyViewModel 中，增加了`CoroutineScope.singleLaunch(...)`拓展函数来简化使用。如**本项目**中的代码：

```kotlin
// UserViewModel.kt

// replay = 1 是为了缓存一份。要不默认是无粘性的，重建之后数据就没了
// 要不就用 StateFlow，但得传默认值，很麻烦
private val _userFlow =
    MutableSharedFlow<Result<UserModel.Data>>(replay = 1)
val userFlow = _userFlow.asSharedFlow()

// single = true 用于 onViewCreated 这种地方，单次执行
// single = false 用于 点击 等延迟事件
fun getUserInfo(id: String, single: Boolean) {
    if (single) {
        viewModelScope.singleLaunch(0) { // 0 是 tag，随便弄一个就行，保证唯一
            NetworkRepo.getUserInfo(id).collect(_userFlow::emit)
        }
    } else {
        viewModelScope.launch {
            NetworkRepo.getUserInfo(id).collect(_userFlow::emit)
        }
    }
}
```

这样灵活性更高，不用多传 Factory，不用`init`代码块，避免了让 ViewModelScope 仅仅起到了 LifecycleScope 的作用。

### SpannedTextGenerator

利用了 Kotlin 的可选参数性质自己封装的一个生成 Spanned Text 的类，有些不足，但某些情况下确实方便够用。如**本项目**中的代码：

```kotlin
// UserActivity.kt

SpannedTextGenerator.KotlinBuilder()
    .addText(data.likeNum, isBold = true, isNewLine = false)
    .addText(" 获赞", isNewLine = false)
    .showIn(binding.tvLike)
SpannedTextGenerator.KotlinBuilder()
    .addText(data.fansNum, isBold = true, isNewLine = false)
    .addText(" 粉丝", isNewLine = false)
    .showIn(binding.tvFans)
SpannedTextGenerator.KotlinBuilder()
    .addText(data.attentionNum, isBold = true, isNewLine = false)
    .addText(" 关注", isNewLine = false)
    .showIn(binding.tvSubscribe)
```

### Kotlin 高阶用法

使用了 Kotlin 的高阶用法提升开发效率。

例如，一行代码分别实现 Fragment 传递与接收 argument，如**本项目**中的代码：

```kotlin
// TextVideoSplitFragment.kt
VideoItemFragment().makeBundle(TO_TEXT_VIDEO_SPLIT_FRAGMENT to type)

// VideoItemFragment.kt
private val type: Int by arguments(TO_TEXT_VIDEO_SPLIT_FRAGMENT, WORK)
```

一句话实现保存到 SharedPreferences，如**本项目**中的代码：

```kotlin
// Preferences.kt

var isLogin: Boolean
    get() = getSpValue("isLogin", false)
    set(value) = putSpValue("isLogin", value)

var loginToken: String
    get() = getSpValue("loginToken", "")
    set(value) = putSpValue("loginToken", value)
```

等等

## 不足 / 待实现

### JZVideoPlayer 的 bug

该项目使用 Jzvd 进行视频播放，模仿短视频软件通过 ViewPager2 进行上下翻页，本人已实现进入该页面时自动播放以及处理切换视频时的播放暂停逻辑，但除了第一次进入该页面的第一个视频能播放之外，其余都只能进行缓冲，无法加载出视频。经过打 Log 分析，不存在视频地址突变 null 的情况，大概率会是 Jzvd 的 bug。

### 部分功能没做

大部分功能的地方还是都能进的。
