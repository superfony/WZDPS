package com.epsmart.wzdp.aidl;

import android.os.Parcel;
import android.os.Parcelable;


public class AppInfo implements Parcelable
{
    private String name;
    
    private int age;
    
    private String place;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }
    
    /**
     * <默认构造函数>
     */
    public AppInfo()
    {
        
    }
    
    /**
     * <默认构造函数>
     */
    public AppInfo(Parcel in)
    {
        //注意顺序
        name = in.readString();
        age = in.readInt();
        place = in.readString();
    }
    
    /**
     * seems meaningless
     * return 0;
     */
    @Override
    public int describeContents()
    {
        return 0;
    }

    /**
     * 将对象序列化为一个Parcel对象
     *  可以将Parcel看成是一个流，通过writeToParcel把对象写到流里面,
     *  再通过createFromParcel从流里读取对象
     *  注意:写的顺序和读的顺序必须一致。 
     */
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(place);
    }
    
    /**
     * 实例化静态内部对象CREATOR实现接口Parcelable.Creator
     * public static final一个都不能少，内部对象CREATOR的名称也不能改变，必须全部大写
     */
    public static final Parcelable.Creator<AppInfo> CREATOR = new Creator<AppInfo>(){

      //将Parcel对象反序列化为HarlanInfo   
        @Override
        public AppInfo createFromParcel(Parcel source)
        {
        	AppInfo hlInfo = new AppInfo(source);
            return hlInfo;
        }

        @Override
        public AppInfo[] newArray(int size)
        {
            return new AppInfo[size];
        }
        
    };
    
}