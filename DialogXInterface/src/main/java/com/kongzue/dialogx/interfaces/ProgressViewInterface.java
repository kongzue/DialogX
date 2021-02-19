package com.kongzue.dialogx.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/11/3 20:39
 */
public interface ProgressViewInterface {
    
    //停止加载动画
    void noLoading();
    
    //切换至完成状态
    void success();
    
    //切换至警告状态
    void warning();
    
    //切换至错误状态
    void error();
    
    //切换至进度（取值 0f-1f）
    void progress(float progress);
    
    //切换至加载状态
    void loading();
    
    //不同状态切换时，衔接动画完成后执行
    ProgressViewInterface whenShowTick(Runnable runnable);
    
    //设置颜色
    ProgressViewInterface setColor(int color);
}
