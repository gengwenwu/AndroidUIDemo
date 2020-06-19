## Android中的 Bitmap和Drawable

### 1，Bitmap
- 称作**位图**，它是其存在的实体之一(具体的类)，一般位图的文件格式后缀为bmp


### 2，Drawable
- Android平台下**通用的图形对象**，它只是一个抽象概念(abstract类)，它可以装载常用格式的图像，
比如：if、png、jpg，当然也支持bmp，还提供了一些高级可视化对象，比如：渐变、图形等。

### 3，重要区别
- Bitmap 是 Drawable (BitmapDrawable对象)，反过来 Drawable 不一定是 Bitmap。
- Drawable 在 **内存占用**和 **绘制速度** 两个非常关键的点上胜过 Bitmap  
- Drawable 与 Bitmap 之间可以互相转换

对比项       | 占用内存   | 绘制速度   |支持缩放  |支持旋转  |
------------|-----------|----------|---------|-------- |
Bitmap      | 大     	| 慢       | 是       | 是     |
Drawable    | 小  	    | 快       | 是       | 是      |

### 4，获取和转换

#### (1)，获取Bitmap对象   

	Resources res = getResources();  
	Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.icon); 
	bmp.getHeight(); // 获取高度
	bmp.getWidth(); // 获取宽度


#### (2)，Drawable对象

	Drawable drawable = getResources().getDrawable(R.drawable.icon);
	drawable.getIntrinsicWidth();  // 获取其宽高
	drawable.getIntrinsicHeight();

#### (3)，Bitmap转换成Drawable  

	Bitmap bm = xx; // xx根据你的情况获取
	BitmapDrawable bd = new BitmapDrawable(getResource(), bm); 
	// 因为 BitmapDrawable 是 Drawable 的子类，最终直接使用bd对象即可。

#### (4)，Drawable转化为Bitmap    
转化的方式是把 Drawable 通过画板画出来  

	public static Bitmap drawableToBitmap(Drawable drawable) {  
			// 取 drawable 的长宽  
	        int w = drawable.getIntrinsicWidth();  
	        int h = drawable.getIntrinsicHeight();  
	        
			// 取 drawable 的颜色格式  
        	Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  : Bitmap.Config.RGB_565;  
			// 建立对应 bitmap  
       	 Bitmap bitmap = Bitmap.createBitmap(w, h, config);  
			// 建立对应 bitmap 的画布  
        	Canvas canvas = new Canvas(bitmap);  
        	drawable.setBounds(0, 0, w, h);  
			// 把 drawable 内容画到画布中  
        	drawable.draw(canvas);  
        	
        	return bitmap;  
	} 

参考：  
[Android开发中Bitmap和Drawable的区别](https://blog.csdn.net/liu537192/article/details/47665269)  

[Bitmap和Drawable的关系、区别](https://blog.csdn.net/qq1263292336/article/details/78867461)  
