package Chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yuhe on 2019/1/22.
 *
 * 1 委托使用ConcurrentHashMap实现同步
 * 2 发布的ImutablePoint不能set 保证了线程安全
 * 3 unmodifiedMap本质上是一种装饰者模式，它包装了内部的map 并禁止了put操作
 */
public class DelegatingCarMonitor {
    private final ConcurrentHashMap<String, ImutablePoint> concurrentHashMap; // 安全的容器保证了线程安全
    private final Map<String, ImutablePoint> unmodifiedMap;// 保证了发布出去的map禁止put操作，保证所有put操作通过setLocation方法进入

    public DelegatingCarMonitor(Map<String, ImutablePoint> map) {
        concurrentHashMap = new ConcurrentHashMap<String, ImutablePoint>(map);
        unmodifiedMap = Collections.unmodifiableMap(map);
    }

    public Map<String, ImutablePoint> getLocations(){
        // 保证了发布出去的map禁止put操作，保证所有put操作通过setLocation方法进入
        // 由于unmodifiedMap封装的是concurrentHashMap，所以会动态的更新car的位置
        return unmodifiedMap;
    }

    public Map<String, ImutablePoint> getStaticLocations(){// 静态快照
        // new HashMap导致了新的引用指向了此时的concurrentHashMap对象，当concurrenyMap变化时，不影响静态快照
        return Collections.unmodifiableMap(new HashMap<String, ImutablePoint>(concurrentHashMap));
    }

    public ImutablePoint getLocation(String id) {
        ImutablePoint imutablePoint = concurrentHashMap.get(id);// 发布出去的ImutablePoint禁止了修改操作，从而保证了线程安全
        return imutablePoint;
    }

    public void setLocation(String id, int x, int y) {
        ImutablePoint replace = concurrentHashMap.replace(id, new ImutablePoint(x, y));// 安全的容器保证了内存可见性
        if (replace == null) {
            throw new RuntimeException("没有次id");
        }

    }

}

class ImutablePoint{
    private final int x;
    private final int y;

    public ImutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}