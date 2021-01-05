ViewModel+LiveData+DataBinding综合使用
实现点击按钮后，数值加 1 并显示在屏幕上

###1.创建ViewModel
项目中有一个MainActivity.java类
创建一个MainViewModel继承自ViewModel
```
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    //LiveData维护一个整型数 Number
    private MutableLiveData<Integer> Number;
    public MutableLiveData<Integer> getNumber() {

        if(Number==null){
            Number = new MutableLiveData<>();
            Number.setValue(0);
        }
        return Number;
    }

    /**
     * 实现数据加1
     * @param num
     */
    public void add(int num){
        Number.setValue(Number.getValue()+num);
    }
}
```

###2.配置DataBinding
在app build.gradle中配置 DataBinding
```
 buildFeatures{
        dataBinding = true
        // for view binding :
        // viewBinding = true
    }
```
###3.修改MainActivity布局文件
使用<layout> </layout>包裹整个布局 <layout>下面添加 <data></data>
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
    </data>

<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">


    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:textSize="30sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.501"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.247" />

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.43" />
</androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
重新编译后，在MainActivity.java中可以访问一个对象ActivityMainBinding，这个对象是根据
MainActivity.java的布局文件 activity_main.xml 名字 + Binding生成的。

在MainActivity中定义     ActivityMainBinding binding;
原来的 setContentView(R.layout.activity_main); 使用
binding = DataBindingUtil.setContentView(this, R.layout.activity_main); 代替
通过binding可以访问布局中的控件

MainActivity.java
```
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import com.gao.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //创建ViewModel对象
        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);
        
        //给数据Number设置观察者，数据变化后，屏幕界面更新
        mainViewModel.getNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.textView.setText(String.valueOf(integer));
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mainViewModel.add(1);
            }
        });
    }
}
```

在<data></data>中添加 <variable/>, name是自定义ViewModel名称，type是具体的ViewModel
```
<data>
        <variable
            name="mainviewmodel"
            type="com.gao.databinding.MainViewModel" />
    </data>
```
配置好 <variable/>后TextView的数据显示可以直接绑定MainViewModel中的数据
Button的点击事件也可以直接绑定为MainViewModel中的方法

TextView
```
        android:text="@{String.valueOf(mainviewmodel.number)}"
```

Button
```
        android:onClick="@{()->mainviewmodel.add()}"
```

要让布局中控件与MainViewModel中的数据和事件绑定生效，需要在MainActivity中进行设置
```
 //setMainviewmodel是根据布局中绑定的自定义的ViewModel名字生成的方法
 binding.setMainviewmodel(mainViewModel);
 binding.setLifecycleOwner(this);
```

改造后MainActivity.java
```
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.gao.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //创建ViewModel对象
        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);
        //setMainviewmodel是根据布局中绑定的自定义的ViewModel名字生成的方法
        binding.setMainviewmodel(mainViewModel);
        binding.setLifecycleOwner(this);
    }
}
```

参考：# [LongLongLongLongLongLongLongLongLong](https://github.com/LongLongLongLongLongLongLongLongLong)/**[DataBindingDemo](https://github.com/LongLongLongLongLongLongLongLongLong/DataBindingDemo)**








