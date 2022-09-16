package demo;

import java.util.*;

/**
 * @author wxl
 * Date 2022/8/26 9:45
 * @version 1.0
 * Description:
 */
public class SimpleHashMap<K, V> {


    transient Node<K, V>[] table;


    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     * 初始化容量
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     * 默认加载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * The next size value at which to resize (capacity * load factor).
     * 阈值
     *
     * @serial
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.)
    int threshold;

    /**
     * The load factor for the hash table.
     *
     * @serial
     */
    final float loadFactor;

    transient int modCount;

    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;

    public SimpleHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        //返回目标容量的向下取整的2次幂 例如： tableSizeFor(5)=8
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public SimpleHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public SimpleHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ?
                1 :
                (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    static class Entry<K, V> extends Node<K, V> {
        Entry<K, V> before, after;

        Entry(int hash, K key, V value, Node<K, V> next) {
            super(hash, key, value, next);
        }
    }

    static final class TreeNode<K, V> extends Entry<K, V> {
        TreeNode<K, V> parent;  // red-black tree links
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> prev;    // needed to unlink next upon deletion
        boolean red;


        public TreeNode(int hash, K key, V value, Node<K, V> next, TreeNode<K, V> parent, TreeNode<K, V> left, TreeNode<K, V> right, TreeNode<K, V> prev, boolean red) {
            super(hash, key, value, next);
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.prev = prev;
            this.red = red;
        }
        final void split(SimpleHashMap<K, V> map, Node<K, V>[] tab, int index, int bit) {

        }

        final TreeNode<K, V> putTreeVal(SimpleHashMap<K, V> map, Node<K, V>[] tab,
                                        int h, K k, V v) {
            return null;
        }
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }




    }


    static final int hash(Object key) {
        int h;
        //^ 异或
        //>>> 右移
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    // Create a regular (non-tree) node
    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }


    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }



    final V putVal(int hash,
                   K key,
                   V value,
                   boolean onlyIfAbsent,
                   boolean evict) {
        //链表的数组 -> 拉链表
        Node<K, V>[] tab;
        Node<K, V> p;
        //n 为 拉链表的长度
        int n, i;
        //把之前的拉链表赋值给 当前操作的局部变量 tab、n
        //判断是否为新建的map
        if ((tab = table) == null || (n = tab.length) == 0) {
            //初始化 拉链表的长度
            n = (tab = resize()).length;
        }

        //判断尾部是否为空然后进行  尾插法
        if ((p = tab[i = (n - 1) & hash]) == null)
            //当前节点为空
            tab[i] = newNode(hash, key, value, null);

        else {
            Node<K, V> e;
            K k;
            //判断当前下标的可以 与 put 进来的可以是否相等
            //在后续代码中进行new value值 覆盖 old value
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                //判断当前节点的索引相等的话，就覆盖当前索引
                e = p;

                //如果当期节点是红黑树实例
            else if (p instanceof TreeNode)
                //走红黑树的逻辑红黑树逻辑
                e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            else {
                //否则为链表逻辑
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        //尾部为空就插入到尾部
                        p.next = newNode(hash, key, value, null);
                        //binCount>=7，即链表长度大于8的时候就会树化
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab/*当前数组*/, hash/*当前hash值*/);
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            //如果存在相同的key，则更新，并返回old value
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                //判断当前值是否能覆盖
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        // 增加修改次数
        ++modCount;

        //判断是否需要扩容
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    // Callbacks to allow LinkedHashMap post-actions
    void afterNodeAccess(Node<K, V> p) {
    }

    void afterNodeInsertion(boolean evict) {
    }

    void afterNodeRemoval(Node<K, V> p) {
    }


    final Node<K, V>[] resize() {
        //记录old value
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;

        //初始化 new value
        int newCap, newThr = 0;
        //扩容为 << 2
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            //左移扩大两倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        //处理特殊情况
        //表示要初始化数组，但是用户指定了初始化容量
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
            //新建初始化容量
            //DEFAULT_INITIAL_CAPACITY 16
            //DEFAULT_LOAD_FACTOR  0.75f
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            //初始化阈值 =  loadFactor * threshold = 0.75 * 16 = 12
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        //重组或者初始化拉链表
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;

        //把老值放到新链表的过程
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                //临时节点 用于临时存储老值
                Node<K, V> e;
                //遍历数组
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    //只有一个元素, 则直接转移到新数组上
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                        // 如果该位置上的元素是TreeNode，则对这颗红黑树进行转移
                    else if (e instanceof TreeNode)
                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                        // 否则，该位置上是一个链表，则要转移该链表
                    else { // preserve order

                        // 将当前链表拆分成为两个链表，记录链表的头结点和尾结点
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        }
                        //拆分链表
                        while ((e = next) != null);
                        //将拆分的链表分别加入到新的数组中
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /**
     * Replaces all linked nodes in bin at index for given hash unless
     * table is too small, in which case resizes instead.
     */
    final void treeifyBin(Node<K, V>[] tab, int hash) {
//        int n, index;
//        Node<K,V> e;
//        //// 数组长度如果小于MIN_TREEIFY_CAPACITY(默认为64)，则会扩容
//        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
//            resize();
//        else if ((e = tab[index = (n - 1) & hash]) != null) {
//
//            TreeNode<K,V> hd = null, tl = null;
//            do {
//                //并且把节点类型改为TreeNode
//                TreeNode<K,V> p = replacementTreeNode(e, null);
//                //把链表改造为双向链表
//                if (tl == null)
//                    hd = p;
//                else {
//                    p.prev = tl;
//                    tl.next = p;
//                }
//                tl = p;
//            } while ((e = e.next) != null);
//            if ((tab[index] = hd) != null)
//                hd.treeify(tab);
//        }
    }


}
