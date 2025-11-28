package com.example.followlist.data.network;

import android.os.Handler;
import android.os.Looper;

import com.example.followlist.data.model.FollowRelation;
import com.example.followlist.data.model.User;
import com.example.followlist.data.model.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Mock 服务端实现
 * 模拟网络请求，生成1000条用户数据，支持分页返回
 */
public class MockFollowListApi implements FollowListApi {
    private static final int TOTAL_USERS = 1000;
    private static final int PAGE_SIZE = 10;
    private static final long MOCK_NETWORK_DELAY = 30; // 模拟网络延迟（毫秒）
    
    private static final String[] AVATAR_NAMES = {
            "ogs_001", "ogs_002", "ogs_003", "ogs_004", "ogs_005", "ogs_006", "ogs_007", "ogs_008", "ogs_009","ogs_010", "ogs_011", "ogs_012",
            "ogs_013", "ogs_014", "ogs_015", "ogs_016", "ogs_017", "ogs_018", "ogs_019", "ogs_020", "ogs_021", "ogs_022", "ogs_023", "ogs_024",
            "ogn_001", "ogn_002", "ogn_003", "ogn_004", "ogn_005", "ogn_006", "ogn_007", "ogn_008", "ogn_009", "ogn_010", "ogn_011", "ogn_012",
            "ogn_013", "ogn_014", "ogn_015", "ogn_016", "ogn_017", "ogn_018", "ogn_019", "ogn_020", "ogn_021", "ogn_022", "ogn_023", "ogn_024",
            "ogn_025", "ogn_026", "ogn_027", "ogn_028", "ogn_029", "ogn_030", "ogn_031", "ogn_032", "ogn_033", "ogn_034", "ogn_035", "ogn_036",
            "ogn_037", "ogn_038", "ogn_039", "ogn_040", "ogn_041", "ogn_042", "ogn_043", "ogn_044", "ogn_045", "ogn_046", "ogn_047", "ogn_048",
            "ogn_049", "ogn_050", "ogn_051", "ogn_052", "ogn_053", "ogn_054", "ogn_055", "ogn_056", "ogn_057", "ogn_058", "ogn_059", "ogn_060",
            "ogn_061", "ogn_062", "ogn_063", "ogn_064", "ogn_065", "ogn_066", "ogn_067", "ogn_068", "ogn_069", "ogn_070", "ogn_071", "ogn_072",
            "ogn_073", "ogn_074", "ogn_075", "ogn_076", "ogn_077", "ogn_078", "ogn_079", "ogn_080", "ogn_081", "ogn_082", "ogn_083", "ogn_084",
            "ogn_085", "ogn_086", "ogn_087", "ogn_088", "ogn_089", "ogn_090", "ogn_091", "ogn_092", "ogn_093", "ogn_094", "ogn_095", "ogn_096",
            "ogn_097", "ogn_098", "ogn_099", "ogn_100", "ogn_101", "ogn_102", "ogn_103", "ogn_104", "ogn_105", "ogn_106", "ogn_107", "ogn_108",
            "ogn_109", "ogn_110", "ogn_111", "ogn_112", "ogn_113", "ogn_114", "ogn_115", "ogn_116", "ogn_117", "ogn_118", "ogn_119", "ogn_120",
            "ogn_121", "ogn_122", "ogn_123", "ogn_124", "ogn_125", "ogn_126", "ogn_127", "ogn_128", "ogn_129", "ogn_130", "ogn_131", "ogn_132",
            "ogn_133", "ogn_134", "ogn_135", "ogn_136", "ogn_137", "ogn_138", "ogn_139", "ogn_140", "ogn_141", "ogn_142", "ogn_143", "ogn_144",
            "ogn_145", "ogn_146", "ogn_147", "ogn_148", "ogn_149", "ogn_150", "ogn_151", "ogn_152", "ogn_153", "ogn_154", "ogn_155", "ogn_156",
            "ogn_157", "ogn_158", "ogn_159", "ogn_160", "ogn_161", "ogn_162", "ogn_163", "ogn_164", "ogn_165", "ogn_166", "ogn_167", "ogn_168",
            "ogn_169", "ogn_170", "ogn_171", "ogn_172", "ogn_173", "ogn_174", "ogn_175", "ogn_176", "ogn_177", "ogn_178", "ogn_179", "ogn_180",
            "ogn_181", "ogn_182", "ogn_183", "ogn_184", "ogn_185", "ogn_186", "ogn_187", "ogn_188", "ogn_189", "ogn_190", "ogn_191", "ogn_192",
            "ogn_193", "ogn_194", "ogn_195", "ogn_196", "ogn_197", "ogn_198", "ogn_199", "ogn_200", "ogn_201", "ogn_202", "ogn_203", "ogn_204",
            "ogn_205", "ogn_206", "ogn_207", "ogn_208", "ogn_209", "ogn_210", "ogn_211", "ogn_212", "ogn_213", "ogn_214", "ogn_215", "ogn_216",
            "ogn_217", "ogn_218", "ogn_219", "ogn_220", "ogn_221", "ogn_222", "ogn_223", "ogn_224", "ogn_225", "ogn_226", "ogn_227", "ogn_228",
            "ogn_229", "ogn_230", "ogn_231", "ogn_232", "ogn_233", "ogn_234", "ogn_235", "ogn_236", "ogn_237", "ogn_238", "ogn_239", "ogn_240",
            "ogn_241", "ogn_242", "ogn_243", "ogn_244", "ogn_245", "ogn_246", "ogn_247", "ogn_248", "ogn_249", "ogn_250", "ogn_251", "ogn_252",
            "ogn_253", "ogn_254", "ogn_255", "ogn_256", "ogn_257", "ogn_258", "ogn_259", "ogn_260", "ogn_261", "ogn_262", "ogn_263", "ogn_264",
            "ogn_265", "ogn_266", "ogn_267", "ogn_268", "ogn_269", "ogn_270", "ogn_271", "ogn_272", "ogn_273", "ogn_274", "ogn_275", "ogn_276",
            "ogn_277", "ogn_278", "ogn_279", "ogn_280", "ogn_281", "ogn_282", "ogn_283", "ogn_284", "ogn_285", "ogn_286", "ogn_287", "ogn_288",
            "ogn_289", "ogn_290", "ogn_291", "ogn_292", "ogn_293", "ogn_294", "ogn_295", "ogn_296", "ogn_297", "ogn_298",
            "ogn_299", "ogn_299s", "ogn_300", "ogn_300s", "ogn_301", "ogn_301s", "ogn_302", "ogn_302s", "ogn_303", "ogn_303s", "ogn_304", "ogn_304s",
            "ogn_305", "ogn_305s", "ogn_306", "ogn_306s", "ogn_307", "ogn_307s", "ogn_308", "ogn_308s", "ogn_309", "ogn_309s", "ogn_310", "ogn_310s",
            "ogn_311", "back"
    };
    
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private List<UserBean> allUsers;
    
    public MockFollowListApi() {
        generateAllUsers();
    }
    
    /**
     * 预生成1000条用户数据
     */
    private void generateAllUsers() {
        allUsers = new ArrayList<>();
        String currentUserId = "1042689609"; // 当前用户ID
        
        for (int i = 1; i <= TOTAL_USERS; i++) {
            // 生成用户
            String userId = "user_" + String.format("%06d", i);
            String name = generateUserName(i);
            String avatarName = AVATAR_NAMES[i % AVATAR_NAMES.length];
            
            User user = new User(userId, name, avatarName);
            
            // 生成关注关系
            String followTime = generateFollowTime(i);
            FollowRelation followRelation = new FollowRelation(
                i,                                    // id
                currentUserId,                        // followerId
                userId,                              // followedUserId
                1,                                   // isFollow (所有用户都是已关注状态)
                i % 10 == 0 ? 1 : 0,                 // isSpecialFollow (每10个有一个特别关注)
                "",                                  // note
                followTime                           // followTime
            );

            // 生成UserBean
            UserBean userBean = new UserBean(user, followRelation);
            allUsers.add(userBean);
        }
        
        // 按关注时间倒序排列（模拟服务端排序）
        allUsers.sort((a, b) -> {
            String timeA = a.getFollowTime();
            String timeB = b.getFollowTime();
            if (timeA == null) timeA = "";
            if (timeB == null) timeB = "";
            return timeB.compareTo(timeA); // 倒序：最新的在前
        });
    }
    
    /**
     * 生成用户名
     */
    private String generateUserName(int index) {
        return "用户" + index;
    }
    
    /**
     * 生成关注时间
     */
    private String generateFollowTime(int index) {
        // 生成从2021年到2024年的随机时间
        int year = 2021 + (index % 4);
        int month = 1 + (index % 12);
        int day = 1 + (index % 28);
        int hour = index % 24;
        int minute = index % 60;
        int second = (index * 7) % 60;
        
        return String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }

    /**
     * 获取关注列表总数据量
     */
    @Override
    public int getTotal() {
        return allUsers.size();
    }

    /**
     * 改变关注状态
     * @param userBean 关注/取关的目标
     */
    @Override
    public void setFollow(UserBean userBean) {
        userBean.setFollow(!userBean.isFollow());
    }

    /**
     * 设置特别关注
     * @param userBean 设置特别关注的目标
     * @param isSpecialFollow 设置/取消设置
     */
    @Override
    public void setSpecialFollow(UserBean userBean, boolean isSpecialFollow) {
        userBean.setSpecialFollow(isSpecialFollow);
    }

    /**
     * 设置备注
     * @param userBean 设置备注的目标
     * @param note 备注内容
     */
    @Override
    public void setNote(UserBean userBean, String note) {
        userBean.setNote(note);
    }

    /**
     * 同步获取关注列表
     * @param page 页码
     * @param pageSize 每页大小
     */
    @Override
    public ApiResponse getFollowingList(int page, int pageSize) {
        if (page == 1) {
            // 执行刷新操作
            // 先过滤再排序
            allUsers = allUsers.stream()
                    .filter(UserBean::isFollow)
                    .sorted((a, b) -> {
                        String timeA = a.getFollowTime();
                        String timeB = b.getFollowTime();
                        if (timeA == null) timeA = "";
                        if (timeB == null) timeB = "";
                        return timeB.compareTo(timeA);
                    })
                    .collect(Collectors.toList());
        }

        int current_total_users = getTotal();

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, current_total_users);
        
        if (startIndex >= allUsers.size()) {
            // 超出范围，返回空列表
            return new ApiResponse(new ArrayList<>(), current_total_users, page, pageSize, false);
        }
        
        List<UserBean> pageData = new ArrayList<>(allUsers.subList(startIndex, endIndex));
        boolean hasMore = endIndex < allUsers.size();
        
        return new ApiResponse(pageData, current_total_users, page, pageSize, hasMore);
    }
    
    /**
     * 异步获取关注列表（带回调）
     * @param page 页码
     * @param pageSize 每页大小
     * @param callback 回调接口
     */
    public void getFollowingListAsync(int page, int pageSize, ApiCallback callback) {
        // 在后台线程模拟网络延迟
        new Thread(() -> {
            try {
                // 模拟网络延迟
                Thread.sleep(MOCK_NETWORK_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 获取数据
            ApiResponse response = getFollowingList(page, pageSize);
            
            // 切换到主线程回调
            mainHandler.post(() -> {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            });
        }).start();
    }
    
    /**
     * API 回调接口
     */
    public interface ApiCallback {
        void onSuccess(ApiResponse response);
        void onError(String error);
    }
}

