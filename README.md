# 仿写抖音关注列表页面

## 1. 项目简介

本项目是字节跳动客户端训练营作业，实现了一个仿抖音的关注列表页面。应用使用 Android 原生开发，通过 ViewPager2 和 TabLayout 实现多标签页切换，使用 RecyclerView 展示用户列表。

**第二次作业更新**：基于第一次作业，新增了服务端数据源支持，实现了1000条数据的分页加载功能。

## 2. 核心功能

1. **多标签页切换**
   - 互关：显示互相关注的用户（当前为占位页面）
   - 关注：显示当前用户关注的所有用户（主要功能页面）
   - 粉丝：显示关注当前用户的用户（当前为占位页面）
   - 朋友：显示朋友列表（当前为占位页面）

2. **关注列表展示**
   - 使用 RecyclerView 展示用户列表
   - 显示用户头像、昵称/备注名
   - 显示关注状态和特别关注标识
   - 按关注时间倒序排列
   - **分页加载**：支持服务端分页请求，每次加载10条数据，支持1000条数据量
   - **头像快速加载**：使用 Glide 图片加载库，支持头像预加载和缓存机制

3. **关注管理功能**
   - **关注/取关**：点击关注按钮可切换关注状态
   - **特别关注**：可设置/取消特别关注
   - **设置备注**：可为关注的用户设置备注名，备注名会优先显示
   - **下拉刷新**：支持下拉刷新更新关注列表

4. **服务端数据源**（第二次作业新增）
   - 使用 Mock 服务端模拟真实网络请求
   - 支持1000条用户数据的生成和管理
   - 实现分页请求接口，每页返回10条数据
   - 模拟网络延迟，提供异步回调机制

5. **性能优化**（第二次作业新增）
   - **头像加载优化**：头像预加载机制，滑动时快速显示，用户无感知加载延迟
   - **内存优化**：分页加载避免一次性加载所有数据，Glide 缓存策略优化内存使用

## 3. 技术架构

### 3.1 开发环境
- **开发语言**：Java
- **最低 SDK 版本**：24 (Android 7.0)
- **目标 SDK 版本**：36
- **编译 SDK 版本**：36

### 3.2 主要依赖库

```gradle
- androidx.viewpager2:viewpager2:1.1.0 （用于支持多标签页切换）
- androidx.recyclerview:recyclerview:1.3.2 （用于支持展示用户列表）
- androidx.swiperefreshlayout:swiperefreshlayout （用于支持下拉刷新）
- com.github.bumptech.glide:glide:4.16.0 （用于图片加载和缓存，第二次作业新增）
- de.hdodenhof:circleimageview:3.1.0 （用于圆形头像显示）
```

### 3.3 项目结构

```
app/src/main/java/com/example/followlist/
│
├── data/
│   ├── model/                              # 数据模型
│   │   ├── User.java                               # 用户基本信息模型
│   │   ├── FollowRelation.java                     # 关注关系模型
│   │   └── UserBean.java                           # 用户列表项模型（User+FollowRelation）
│   └── network/                            # 网络层（第二次作业新增）
│       ├── FollowListApi.java                      # API接口定义
│       ├── MockFollowListApi.java                  # Mock服务端实现
│       └── ApiResponse.java                        # API响应模型
│
├── ui/
│   ├── fragment/                           # Fragment相关
│   │   ├── FollowingFragment.java                  # 关注列表Fragment（主要功能页面）
│   │   ├── BlankFragment.java                      # 占位Fragment
│   │   └── recyclerview/                           # RecyclerView相关
│   │       ├── UserAdapter.java                            # RecyclerView适配器
│   │       └── UserViewHolder.java                         # ViewHolder
│   ├── dialog/                             # Dialog相关
│   │   ├── MoreOptionDialog.java                   # 更多选项对话框
│   │   └── SetNoteDialog.java                      # 设置备注对话框
│   └── viewpager/                          # ViewPager相关
│       └── ViewPagerAdapter.java                   # ViewPager2适配器
│
└── MainActivity.java                   # 主Activity，包含ViewPager2和TabLayout
```

### 3.4 服务端数据设计（第二次作业）

#### 3.4.1 Mock 服务端实现
- **数据总量**：1000条用户数据
- **分页大小**：每页10条数据
- **网络延迟**：模拟30ms网络延迟
- **数据生成**：应用启动时预生成所有用户数据，按关注时间倒序排列

#### 3.4.2 API 接口设计
- `getFollowingList(int page)`: 同步获取关注列表（分页）
- `getFollowingListAsync(int page, ApiCallback callback)`: 异步获取关注列表（分页）
- `getTotal()`: 获取关注总数
- `setFollow(UserBean userBean)`: 设置关注状态
- `setSpecialFollow(UserBean userBean, boolean isSpecialFollow)`: 设置特别关注
- `setNote(UserBean userBean, String note)`: 设置备注

#### 3.4.3 API 响应模型
| 字段名 | 类型 | 说明 |
|--------|------|------|
| data | List<UserBean> | 当前页数据列表 |
| total | int | 总数据量 |
| page | int | 当前页码 |
| pageSize | int | 每页大小 |
| hasMore | boolean | 是否还有更多数据 |


## 4. 主要功能实现

### 4.1 用户列表展示

- 使用 `RecyclerView` 和 `LinearLayoutManager` 实现垂直列表
- 通过 `UserAdapter` 和 `UserViewHolder` 实现列表项的展示和交互
- 支持显示用户头像、昵称/备注名、关注状态

### 4.2 关注/取关功能

- 点击关注按钮可切换关注状态
- 状态变更会同步更新到服务端数据（第二次作业改为服务端数据源）
- 取关后会自动取消特别关注状态
- 取关后无法使用更多选项功能

### 4.3 特别关注功能

- 通过更多选项对话框设置/取消特别关注
- 特别关注状态会显示在用户列表中
- 状态变更会同步更新到服务端数据（第二次作业改为服务端数据源）

### 4.4 备注功能

- 可为关注的用户设置备注名
- 备注名会优先显示，替代原昵称
- 如果备注名与昵称相同，则清空备注
- 通过对话框输入备注，支持实时更新

### 4.5 下拉刷新

- 使用 `SwipeRefreshLayout` 实现下拉刷新
- 刷新时会过滤已取关的用户（第二次作业改为服务端数据源）
- 重新加载关注列表并更新关注人数显示
- 重置分页状态，从第一页开始加载

### 4.6 分页加载（第二次作业新增）

- 使用服务端分页接口，每次请求10条数据
- 当用户滑动到距离底部3个item时自动触发加载更多
- 支持下拉刷新重置分页状态
- 显示加载状态
- 异步加载数据，避免阻塞主线程

### 4.7 头像加载优化（第二次作业新增）

- 使用 Glide 图片加载库进行头像加载
- **预加载机制**：在滚动时预加载可见区域前后各5个item的头像
- **缓存策略**：使用 `DiskCacheStrategy.RESOURCE` 缓存转换后的图片
- **占位图**：加载过程中显示占位图，提升用户体验
- **复用请求选项**：使用静态 `RequestOptions` 对象复用，减少内存分配

### 4.8 性能优化（第二次作业新增）

#### 4.8.1 RecyclerView 流畅度优化
- `setHasFixedSize(true)`: 固定列表项高度，跳过 onMeasure() 重新计算
- `setItemViewCacheSize(20)`: 增加 View 缓存数量，缓存刚滚出屏幕的 View
- 使用 `notifyItemRangeInserted()` 替代 `notifyDataSetChanged()`，减少重绘范围
- 分页加载避免一次性渲染大量数据

#### 4.8.2 内存优化
- **分页加载**：只加载当前页和已加载的数据，避免一次性加载全部数据
- **Glide 缓存**：使用内存和磁盘缓存，减少重复加载
- **预加载控制**：限制预加载范围，避免过度预加载占用内存

#### 4.8.3 用户体验优化
- **头像预加载**：在用户滑动前提前加载即将显示的头像
- **异步加载**：网络请求在后台线程执行，不阻塞 UI
- **流畅滑动**：滑动时 FPS > 59

