package Chapter4;

import java.util.*;

/**
 * Created by yuhe on 2019/1/22.
 * 模拟车辆监控系统
 * 1、String 代表车辆 MutablePoint表示车辆位置
 * 2、虽然MutablePoint不是线程安全的，但CarMonitor是线程安全的，它所包含的map和可变的Point对象都未曾发布
 */
public class CarMonitor {
    private final Map<String, MutablePoint> map;

    public CarMonitor(Map<String, MutablePoint> map) {
        this.map = deepCopy(map);
    }

    public synchronized Map<String, MutablePoint> getLocations(){
        return deepCopy(map);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint point = map.get(id);
        return point == null ? null : new MutablePoint(point.getX(), point.getY());
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint point = map.get(id);
        if (point != null) {
            point.setX(x);
            point.setY(y);
        } else {
            throw new RuntimeException("没有次id");
        }
    }

    /**
     * 深拷贝map,返回unmodifiedMap 禁止put操作，只允许get
     * @param map
     * @return
     */
    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> map) {
        Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            MutablePoint point = map.get(key);
            result.put(key, new MutablePoint(point.getX(), point.getY())); // 深拷贝
        }
        return Collections.unmodifiableMap(result); // unmobifiableMap是浅拷贝操作
    }
}

/**
 * 可变对象
 */
class MutablePoint {
    private int x;
    private int y;

    public MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}