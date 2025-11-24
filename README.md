# 仿写抖音关注列表页面

## 1. 项目简介

本项目是字节跳动客户端训练营第一次作业，实现了一个仿抖音的关注列表页面。应用使用 Android 原生开发，采用 SQLite 数据库存储用户信息和关注关系，通过 ViewPager2 和 TabLayout 实现多标签页切换，使用 RecyclerView 展示用户列表。

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

3. **关注管理功能**
   - **关注/取关**：点击关注按钮可切换关注状态
   - **特别关注**：可设置/取消特别关注
   - **设置备注**：可为关注的用户设置备注名，备注名会优先显示
   - **下拉刷新**：支持下拉刷新更新关注列表

4. **数据持久化**
   - 使用 SQLite 数据库存储用户信息和关注关系
   - 支持数据的增删改查操作
   - 自动初始化示例数据

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
│   └── database/                           # 数据库相关
│       ├── FollowListDatabaseHelper.java           # SQLite数据库帮助类
│       └── FollowListDAO.java                      # 数据访问对象，封装数据库操作
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

### 3.3 数据库设计

#### 3.3.1 用户表 (users)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| user_id | VARCHAR(16) | 用户ID（主键） |
| name | TEXT | 用户昵称 |
| avatar_name | TEXT | 头像资源名称 |

#### 3.3.2 关注关系表 (follow_relations)
| 字段名 | 类型 | 说明 |
|--------|------|------|
| follow_relation_id | INTEGER | 关系ID（主键，自增） |
| follower_id | VARCHAR(16) | 关注者ID |
| followed_user_id | VARCHAR(16) | 被关注用户ID |
| is_follow | INTEGER | 是否关注（0/1） |
| is_special_follow | INTEGER | 是否特别关注（0/1） |
| note | TEXT | 备注名 |
| follow_time | TEXT | 关注时间 |

## 4. 主要功能实现

### 4.1 用户列表展示

- 使用 `RecyclerView` 和 `LinearLayoutManager` 实现垂直列表
- 通过 `UserAdapter` 和 `UserViewHolder` 实现列表项的展示和交互
- 支持显示用户头像、昵称/备注名、关注状态

### 4.2 关注/取关功能

- 点击关注按钮可切换关注状态
- 状态变更会同步更新到数据库
- 取关后会自动取消特别关注状态
- 取关后无法使用更多选项功能

### 4.3 特别关注功能

- 通过更多选项对话框设置/取消特别关注
- 特别关注状态会显示在用户列表中
- 状态变更会同步更新到数据库

### 4.4 备注功能

- 可为关注的用户设置备注名
- 备注名会优先显示，替代原昵称
- 如果备注名与昵称相同，则清空备注
- 通过对话框输入备注，支持实时更新

### 4.5 下拉刷新

- 使用 `SwipeRefreshLayout` 实现下拉刷新
- 刷新时会删除已取关的关系记录
- 重新加载关注列表并更新关注人数显示

### 4.6 数据持久化

- 使用 SQLite 数据库存储数据
- 通过 `FollowListDAO` 封装所有数据库操作
- 支持数据的增删改查
- 应用首次启动时自动初始化示例数据
