package github;

public class SingletonDemo {

	/**
	 * @ClassName: HungryWithStaticConstant
	 * @Description: 优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。
	 * 		 缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:43:50
	 */
	static class HungryWithStaticConstant{
		private final static HungryWithStaticConstant INSTANCE  = new HungryWithStaticConstant();
		private HungryWithStaticConstant(){}
		public static HungryWithStaticConstant getInstance() {
			return INSTANCE ;
		}
	}

	/**
	 * @ClassName: HungryWithStaticBlock
	 * @Description: 将类实例化的过程放在了静态代码块中，也是在类装载的时候，就执行静态代码块中的代码，初始化类的实例。
	 * 	         缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:42:53
	 */
	static class HungryWithStaticBlock{
		private static HungryWithStaticBlock instance;
		private HungryWithStaticBlock(){};
		static{
			instance = new HungryWithStaticBlock();
		}
		public static HungryWithStaticBlock getInstance(){
			return instance;
		}
	}
	
	/**
	 * @ClassName: LazyWithUnsafe
	 * @Description: 这种写法起到了Lazy Loading的效果，但是只能在单线程下使用。
	 * 		 如果在多线程下，一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，
	 * 		 另一个线程也通过了这个判断语句，这时便会产生多个实例。所以在多线程环境下不可使用这种方式。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:41:38
	 */
	static class LazyWithUnsafe{
		private static LazyWithUnsafe instance;
		private LazyWithUnsafe(){};
		public static LazyWithUnsafe getInstance() {
			if(instance == null){
				instance = new LazyWithUnsafe();
			}
			return instance;
		}
	}
	
	/**
	 * @ClassName: LazyWithSynchronizedMethod
	 * @Description: 缺点：效率太低了，每个线程在想获得类的实例时候，执行getInstance()方法都要进行同步。
	 * 		 而其实这个方法只执行一次实例化代码就够了，后面的想获得该类实例，直接return就行了。方法进行同步效率太低要改进。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:39:56
	 */
	static class LazyWithSynchronizedMethod{
		private static LazyWithSynchronizedMethod instance;
		private LazyWithSynchronizedMethod(){};
		public static synchronized LazyWithSynchronizedMethod getInstance() {
			if(instance == null){
				instance = new LazyWithSynchronizedMethod();
			}
			return instance;
		}
	}
	
	/**
	 * @ClassName: LazyWithSynchronizedBlock
	 * @Description: 用同步方法同步效率太低，所以摒弃同步方法，改为同步产生实例化的的代码块。但是这种同步并不能起到线程同步的作用。
	 * 		 假如一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，另一个线程也通过了这个判断语句，
	 * 		 这时便会产生多个实例。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:38:23
	 */
	static class LazyWithSynchronizedBlock{
		private static LazyWithSynchronizedBlock instance;
		private LazyWithSynchronizedBlock(){};
		public static LazyWithSynchronizedBlock getInstance() {
			if(instance == null){
				synchronized (LazyWithSynchronizedBlock.class) {
					instance = new LazyWithSynchronizedBlock();
				}
			}
			return instance;
		}
	}
	
	/**
	 * @ClassName: DoubleCheck
	 * @Description: Double-Check概念对于多线程开发者来说不会陌生，如代码中所示，我们进行了两次if (instance == null)检查，
	 * 		 这样就可以保证线程安全了。
	 * 		 这样，实例化代码只用执行一次，后面再次访问时，判断if (singleton == null)，直接return实例化对象。
	 * 		 优点：线程安全；延迟加载；效率较高。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:35:14
	 */
	static class DoubleCheck{
		private static volatile DoubleCheck instance;
		private DoubleCheck(){}
		public static DoubleCheck getInstance() {
			if(instance == null){
				synchronized (DoubleCheck.class) {
					if(instance == null){
						instance = new DoubleCheck();
					}
				}
			}
			return instance;
		}
	}
	
	/**
	 * @ClassName: StaticInnerClass
	 * @Description: 这种方式跟饿汉式方式采用的机制类似，但又有不同。
	 * 		 两者都是采用了类装载的机制来保证初始化实例时只有一个线程。
	 *   		 不同的地方在饿汉式方式是只要StaticInnerClass类被装载就会实例化，没有Lazy-Loading的作用，
	 *   		 而静态内部类方式在StaticInnerClass类被装载时并不会立即实例化，而是在需要实例化时，调用getInstance方法，
	 *   		 才会装载SingletonInstance类，从而完成StaticInnerClass的实例化。
	 *   		 类的静态属性只会在第一次加载类的时候初始化，所以在这里，JVM帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的。
	 *   		 优点：避免了线程不安全，延迟加载，效率高。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:31:27
	 */
	static class StaticInnerClass{
		private StaticInnerClass(){}
		private static class SingletonInstance{
			private static final StaticInnerClass INSTANCE = new StaticInnerClass();
		}
		public static StaticInnerClass getInstance() {
			return SingletonInstance.INSTANCE;
		}
	}
	
	/**
	 * @ClassName: Singleton
	 * @Description: 借助JDK1.5中添加的枚举来实现单例模式。
	 * 		 不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象。
	 * 		 可能是因为枚举在JDK1.5中才添加，所以在实际项目开发中，很少见人这么写过。
	 * @author: czg
	 * @date: 2018年12月22日  下午4:30:32
	 */
	enum Singleton{
		INSTANCE;
		public void whateverMethod(){
			
		}
	}
}
