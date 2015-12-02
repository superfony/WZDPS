/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/fony/Desktop/fony/as/asprojects/Android-Gradle-Examples-master/WZDP1/wZDP/src/main/aidl/com/epsmart/wzdp/aidl/ExecuteServiceAIDL.aidl
 */
package com.epsmart.wzdp.aidl;
public interface ExecuteServiceAIDL extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.epsmart.wzdp.aidl.ExecuteServiceAIDL
{
private static final java.lang.String DESCRIPTOR = "com.epsmart.wzdp.aidl.ExecuteServiceAIDL";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.epsmart.wzdp.aidl.ExecuteServiceAIDL interface,
 * generating a proxy if needed.
 */
public static com.epsmart.wzdp.aidl.ExecuteServiceAIDL asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.epsmart.wzdp.aidl.ExecuteServiceAIDL))) {
return ((com.epsmart.wzdp.aidl.ExecuteServiceAIDL)iin);
}
return new com.epsmart.wzdp.aidl.ExecuteServiceAIDL.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getServerHarlanInfo:
{
data.enforceInterface(DESCRIPTOR);
com.epsmart.wzdp.aidl.AppInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = com.epsmart.wzdp.aidl.AppInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
com.epsmart.wzdp.aidl.ICallBack _arg1;
_arg1 = com.epsmart.wzdp.aidl.ICallBack.Stub.asInterface(data.readStrongBinder());
com.epsmart.wzdp.aidl.UserInfo _result = this.getServerHarlanInfo(_arg0, _arg1);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.epsmart.wzdp.aidl.ExecuteServiceAIDL
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
	 *get info from server and 
	 *Transfer a callback methods handle;
	 *if occur error ,will be return null
	 *对于非基本数据类型和String和CharSequence类型,要加上方向指示
	 *包括in、out和inout，in表示由客户端设置，out表示由服务端设置，inout是两者均可设置。
     */
@Override public com.epsmart.wzdp.aidl.UserInfo getServerHarlanInfo(com.epsmart.wzdp.aidl.AppInfo info, com.epsmart.wzdp.aidl.ICallBack icallback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.epsmart.wzdp.aidl.UserInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((icallback!=null))?(icallback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_getServerHarlanInfo, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.epsmart.wzdp.aidl.UserInfo.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getServerHarlanInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/**
	 *get info from server and 
	 *Transfer a callback methods handle;
	 *if occur error ,will be return null
	 *对于非基本数据类型和String和CharSequence类型,要加上方向指示
	 *包括in、out和inout，in表示由客户端设置，out表示由服务端设置，inout是两者均可设置。
     */
public com.epsmart.wzdp.aidl.UserInfo getServerHarlanInfo(com.epsmart.wzdp.aidl.AppInfo info, com.epsmart.wzdp.aidl.ICallBack icallback) throws android.os.RemoteException;
}
