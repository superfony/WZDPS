/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.epsmart.wzdp.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BizPluginTo implements org.apache.thrift.TBase<BizPluginTo, BizPluginTo._Fields>, java.io.Serializable, Cloneable, Comparable<BizPluginTo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("BizPluginTo");

  private static final org.apache.thrift.protocol.TField PLUGIN_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("pluginId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField PLUGIN_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("pluginName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PLUGIN_VERSION_FIELD_DESC = new org.apache.thrift.protocol.TField("pluginVersion", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField IS_FORCE_UPDATE_FIELD_DESC = new org.apache.thrift.protocol.TField("isForceUpdate", org.apache.thrift.protocol.TType.BOOL, (short)4);
  private static final org.apache.thrift.protocol.TField APP_PACKAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("appPackage", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField APP_MAIN_ACTIVITY_FIELD_DESC = new org.apache.thrift.protocol.TField("appMainActivity", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new BizPluginToStandardSchemeFactory());
    schemes.put(TupleScheme.class, new BizPluginToTupleSchemeFactory());
  }

  public long pluginId; // required
  public String pluginName; // required
  public int pluginVersion; // required
  public boolean isForceUpdate; // required
  public String appPackage; // required
  public String appMainActivity; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PLUGIN_ID((short)1, "pluginId"),
    PLUGIN_NAME((short)2, "pluginName"),
    PLUGIN_VERSION((short)3, "pluginVersion"),
    IS_FORCE_UPDATE((short)4, "isForceUpdate"),
    APP_PACKAGE((short)5, "appPackage"),
    APP_MAIN_ACTIVITY((short)6, "appMainActivity");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // PLUGIN_ID
          return PLUGIN_ID;
        case 2: // PLUGIN_NAME
          return PLUGIN_NAME;
        case 3: // PLUGIN_VERSION
          return PLUGIN_VERSION;
        case 4: // IS_FORCE_UPDATE
          return IS_FORCE_UPDATE;
        case 5: // APP_PACKAGE
          return APP_PACKAGE;
        case 6: // APP_MAIN_ACTIVITY
          return APP_MAIN_ACTIVITY;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __PLUGINID_ISSET_ID = 0;
  private static final int __PLUGINVERSION_ISSET_ID = 1;
  private static final int __ISFORCEUPDATE_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PLUGIN_ID, new org.apache.thrift.meta_data.FieldMetaData("pluginId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.PLUGIN_NAME, new org.apache.thrift.meta_data.FieldMetaData("pluginName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PLUGIN_VERSION, new org.apache.thrift.meta_data.FieldMetaData("pluginVersion", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.IS_FORCE_UPDATE, new org.apache.thrift.meta_data.FieldMetaData("isForceUpdate", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.APP_PACKAGE, new org.apache.thrift.meta_data.FieldMetaData("appPackage", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.APP_MAIN_ACTIVITY, new org.apache.thrift.meta_data.FieldMetaData("appMainActivity", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(BizPluginTo.class, metaDataMap);
  }

  public BizPluginTo() {
  }

  public BizPluginTo(
    long pluginId,
    String pluginName,
    int pluginVersion,
    boolean isForceUpdate,
    String appPackage,
    String appMainActivity)
  {
    this();
    this.pluginId = pluginId;
    setPluginIdIsSet(true);
    this.pluginName = pluginName;
    this.pluginVersion = pluginVersion;
    setPluginVersionIsSet(true);
    this.isForceUpdate = isForceUpdate;
    setIsForceUpdateIsSet(true);
    this.appPackage = appPackage;
    this.appMainActivity = appMainActivity;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public BizPluginTo(BizPluginTo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.pluginId = other.pluginId;
    if (other.isSetPluginName()) {
      this.pluginName = other.pluginName;
    }
    this.pluginVersion = other.pluginVersion;
    this.isForceUpdate = other.isForceUpdate;
    if (other.isSetAppPackage()) {
      this.appPackage = other.appPackage;
    }
    if (other.isSetAppMainActivity()) {
      this.appMainActivity = other.appMainActivity;
    }
  }

  public BizPluginTo deepCopy() {
    return new BizPluginTo(this);
  }

  @Override
  public void clear() {
    setPluginIdIsSet(false);
    this.pluginId = 0;
    this.pluginName = null;
    setPluginVersionIsSet(false);
    this.pluginVersion = 0;
    setIsForceUpdateIsSet(false);
    this.isForceUpdate = false;
    this.appPackage = null;
    this.appMainActivity = null;
  }

  public long getPluginId() {
    return this.pluginId;
  }

  public BizPluginTo setPluginId(long pluginId) {
    this.pluginId = pluginId;
    setPluginIdIsSet(true);
    return this;
  }

  public void unsetPluginId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PLUGINID_ISSET_ID);
  }

  /** Returns true if field pluginId is set (has been assigned a value) and false otherwise */
  public boolean isSetPluginId() {
    return EncodingUtils.testBit(__isset_bitfield, __PLUGINID_ISSET_ID);
  }

  public void setPluginIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PLUGINID_ISSET_ID, value);
  }

  public String getPluginName() {
    return this.pluginName;
  }

  public BizPluginTo setPluginName(String pluginName) {
    this.pluginName = pluginName;
    return this;
  }

  public void unsetPluginName() {
    this.pluginName = null;
  }

  /** Returns true if field pluginName is set (has been assigned a value) and false otherwise */
  public boolean isSetPluginName() {
    return this.pluginName != null;
  }

  public void setPluginNameIsSet(boolean value) {
    if (!value) {
      this.pluginName = null;
    }
  }

  public int getPluginVersion() {
    return this.pluginVersion;
  }

  public BizPluginTo setPluginVersion(int pluginVersion) {
    this.pluginVersion = pluginVersion;
    setPluginVersionIsSet(true);
    return this;
  }

  public void unsetPluginVersion() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PLUGINVERSION_ISSET_ID);
  }

  /** Returns true if field pluginVersion is set (has been assigned a value) and false otherwise */
  public boolean isSetPluginVersion() {
    return EncodingUtils.testBit(__isset_bitfield, __PLUGINVERSION_ISSET_ID);
  }

  public void setPluginVersionIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PLUGINVERSION_ISSET_ID, value);
  }

  public boolean isIsForceUpdate() {
    return this.isForceUpdate;
  }

  public BizPluginTo setIsForceUpdate(boolean isForceUpdate) {
    this.isForceUpdate = isForceUpdate;
    setIsForceUpdateIsSet(true);
    return this;
  }

  public void unsetIsForceUpdate() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ISFORCEUPDATE_ISSET_ID);
  }

  /** Returns true if field isForceUpdate is set (has been assigned a value) and false otherwise */
  public boolean isSetIsForceUpdate() {
    return EncodingUtils.testBit(__isset_bitfield, __ISFORCEUPDATE_ISSET_ID);
  }

  public void setIsForceUpdateIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ISFORCEUPDATE_ISSET_ID, value);
  }

  public String getAppPackage() {
    return this.appPackage;
  }

  public BizPluginTo setAppPackage(String appPackage) {
    this.appPackage = appPackage;
    return this;
  }

  public void unsetAppPackage() {
    this.appPackage = null;
  }

  /** Returns true if field appPackage is set (has been assigned a value) and false otherwise */
  public boolean isSetAppPackage() {
    return this.appPackage != null;
  }

  public void setAppPackageIsSet(boolean value) {
    if (!value) {
      this.appPackage = null;
    }
  }

  public String getAppMainActivity() {
    return this.appMainActivity;
  }

  public BizPluginTo setAppMainActivity(String appMainActivity) {
    this.appMainActivity = appMainActivity;
    return this;
  }

  public void unsetAppMainActivity() {
    this.appMainActivity = null;
  }

  /** Returns true if field appMainActivity is set (has been assigned a value) and false otherwise */
  public boolean isSetAppMainActivity() {
    return this.appMainActivity != null;
  }

  public void setAppMainActivityIsSet(boolean value) {
    if (!value) {
      this.appMainActivity = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PLUGIN_ID:
      if (value == null) {
        unsetPluginId();
      } else {
        setPluginId((Long)value);
      }
      break;

    case PLUGIN_NAME:
      if (value == null) {
        unsetPluginName();
      } else {
        setPluginName((String)value);
      }
      break;

    case PLUGIN_VERSION:
      if (value == null) {
        unsetPluginVersion();
      } else {
        setPluginVersion((Integer)value);
      }
      break;

    case IS_FORCE_UPDATE:
      if (value == null) {
        unsetIsForceUpdate();
      } else {
        setIsForceUpdate((Boolean)value);
      }
      break;

    case APP_PACKAGE:
      if (value == null) {
        unsetAppPackage();
      } else {
        setAppPackage((String)value);
      }
      break;

    case APP_MAIN_ACTIVITY:
      if (value == null) {
        unsetAppMainActivity();
      } else {
        setAppMainActivity((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PLUGIN_ID:
      return Long.valueOf(getPluginId());

    case PLUGIN_NAME:
      return getPluginName();

    case PLUGIN_VERSION:
      return Integer.valueOf(getPluginVersion());

    case IS_FORCE_UPDATE:
      return Boolean.valueOf(isIsForceUpdate());

    case APP_PACKAGE:
      return getAppPackage();

    case APP_MAIN_ACTIVITY:
      return getAppMainActivity();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PLUGIN_ID:
      return isSetPluginId();
    case PLUGIN_NAME:
      return isSetPluginName();
    case PLUGIN_VERSION:
      return isSetPluginVersion();
    case IS_FORCE_UPDATE:
      return isSetIsForceUpdate();
    case APP_PACKAGE:
      return isSetAppPackage();
    case APP_MAIN_ACTIVITY:
      return isSetAppMainActivity();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof BizPluginTo)
      return this.equals((BizPluginTo)that);
    return false;
  }

  public boolean equals(BizPluginTo that) {
    if (that == null)
      return false;

    boolean this_present_pluginId = true;
    boolean that_present_pluginId = true;
    if (this_present_pluginId || that_present_pluginId) {
      if (!(this_present_pluginId && that_present_pluginId))
        return false;
      if (this.pluginId != that.pluginId)
        return false;
    }

    boolean this_present_pluginName = true && this.isSetPluginName();
    boolean that_present_pluginName = true && that.isSetPluginName();
    if (this_present_pluginName || that_present_pluginName) {
      if (!(this_present_pluginName && that_present_pluginName))
        return false;
      if (!this.pluginName.equals(that.pluginName))
        return false;
    }

    boolean this_present_pluginVersion = true;
    boolean that_present_pluginVersion = true;
    if (this_present_pluginVersion || that_present_pluginVersion) {
      if (!(this_present_pluginVersion && that_present_pluginVersion))
        return false;
      if (this.pluginVersion != that.pluginVersion)
        return false;
    }

    boolean this_present_isForceUpdate = true;
    boolean that_present_isForceUpdate = true;
    if (this_present_isForceUpdate || that_present_isForceUpdate) {
      if (!(this_present_isForceUpdate && that_present_isForceUpdate))
        return false;
      if (this.isForceUpdate != that.isForceUpdate)
        return false;
    }

    boolean this_present_appPackage = true && this.isSetAppPackage();
    boolean that_present_appPackage = true && that.isSetAppPackage();
    if (this_present_appPackage || that_present_appPackage) {
      if (!(this_present_appPackage && that_present_appPackage))
        return false;
      if (!this.appPackage.equals(that.appPackage))
        return false;
    }

    boolean this_present_appMainActivity = true && this.isSetAppMainActivity();
    boolean that_present_appMainActivity = true && that.isSetAppMainActivity();
    if (this_present_appMainActivity || that_present_appMainActivity) {
      if (!(this_present_appMainActivity && that_present_appMainActivity))
        return false;
      if (!this.appMainActivity.equals(that.appMainActivity))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(BizPluginTo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetPluginId()).compareTo(other.isSetPluginId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPluginId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pluginId, other.pluginId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPluginName()).compareTo(other.isSetPluginName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPluginName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pluginName, other.pluginName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPluginVersion()).compareTo(other.isSetPluginVersion());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPluginVersion()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pluginVersion, other.pluginVersion);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetIsForceUpdate()).compareTo(other.isSetIsForceUpdate());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIsForceUpdate()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.isForceUpdate, other.isForceUpdate);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAppPackage()).compareTo(other.isSetAppPackage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAppPackage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.appPackage, other.appPackage);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAppMainActivity()).compareTo(other.isSetAppMainActivity());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAppMainActivity()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.appMainActivity, other.appMainActivity);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("BizPluginTo(");
    boolean first = true;

    sb.append("pluginId:");
    sb.append(this.pluginId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("pluginName:");
    if (this.pluginName == null) {
      sb.append("null");
    } else {
      sb.append(this.pluginName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("pluginVersion:");
    sb.append(this.pluginVersion);
    first = false;
    if (!first) sb.append(", ");
    sb.append("isForceUpdate:");
    sb.append(this.isForceUpdate);
    first = false;
    if (!first) sb.append(", ");
    sb.append("appPackage:");
    if (this.appPackage == null) {
      sb.append("null");
    } else {
      sb.append(this.appPackage);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("appMainActivity:");
    if (this.appMainActivity == null) {
      sb.append("null");
    } else {
      sb.append(this.appMainActivity);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class BizPluginToStandardSchemeFactory implements SchemeFactory {
    public BizPluginToStandardScheme getScheme() {
      return new BizPluginToStandardScheme();
    }
  }

  private static class BizPluginToStandardScheme extends StandardScheme<BizPluginTo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, BizPluginTo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PLUGIN_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.pluginId = iprot.readI64();
              struct.setPluginIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PLUGIN_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.pluginName = iprot.readString();
              struct.setPluginNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PLUGIN_VERSION
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.pluginVersion = iprot.readI32();
              struct.setPluginVersionIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // IS_FORCE_UPDATE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.isForceUpdate = iprot.readBool();
              struct.setIsForceUpdateIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // APP_PACKAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.appPackage = iprot.readString();
              struct.setAppPackageIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // APP_MAIN_ACTIVITY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.appMainActivity = iprot.readString();
              struct.setAppMainActivityIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, BizPluginTo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(PLUGIN_ID_FIELD_DESC);
      oprot.writeI64(struct.pluginId);
      oprot.writeFieldEnd();
      if (struct.pluginName != null) {
        oprot.writeFieldBegin(PLUGIN_NAME_FIELD_DESC);
        oprot.writeString(struct.pluginName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PLUGIN_VERSION_FIELD_DESC);
      oprot.writeI32(struct.pluginVersion);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(IS_FORCE_UPDATE_FIELD_DESC);
      oprot.writeBool(struct.isForceUpdate);
      oprot.writeFieldEnd();
      if (struct.appPackage != null) {
        oprot.writeFieldBegin(APP_PACKAGE_FIELD_DESC);
        oprot.writeString(struct.appPackage);
        oprot.writeFieldEnd();
      }
      if (struct.appMainActivity != null) {
        oprot.writeFieldBegin(APP_MAIN_ACTIVITY_FIELD_DESC);
        oprot.writeString(struct.appMainActivity);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class BizPluginToTupleSchemeFactory implements SchemeFactory {
    public BizPluginToTupleScheme getScheme() {
      return new BizPluginToTupleScheme();
    }
  }

  private static class BizPluginToTupleScheme extends TupleScheme<BizPluginTo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, BizPluginTo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetPluginId()) {
        optionals.set(0);
      }
      if (struct.isSetPluginName()) {
        optionals.set(1);
      }
      if (struct.isSetPluginVersion()) {
        optionals.set(2);
      }
      if (struct.isSetIsForceUpdate()) {
        optionals.set(3);
      }
      if (struct.isSetAppPackage()) {
        optionals.set(4);
      }
      if (struct.isSetAppMainActivity()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetPluginId()) {
        oprot.writeI64(struct.pluginId);
      }
      if (struct.isSetPluginName()) {
        oprot.writeString(struct.pluginName);
      }
      if (struct.isSetPluginVersion()) {
        oprot.writeI32(struct.pluginVersion);
      }
      if (struct.isSetIsForceUpdate()) {
        oprot.writeBool(struct.isForceUpdate);
      }
      if (struct.isSetAppPackage()) {
        oprot.writeString(struct.appPackage);
      }
      if (struct.isSetAppMainActivity()) {
        oprot.writeString(struct.appMainActivity);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, BizPluginTo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.pluginId = iprot.readI64();
        struct.setPluginIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.pluginName = iprot.readString();
        struct.setPluginNameIsSet(true);
      }
      if (incoming.get(2)) {
        struct.pluginVersion = iprot.readI32();
        struct.setPluginVersionIsSet(true);
      }
      if (incoming.get(3)) {
        struct.isForceUpdate = iprot.readBool();
        struct.setIsForceUpdateIsSet(true);
      }
      if (incoming.get(4)) {
        struct.appPackage = iprot.readString();
        struct.setAppPackageIsSet(true);
      }
      if (incoming.get(5)) {
        struct.appMainActivity = iprot.readString();
        struct.setAppMainActivityIsSet(true);
      }
    }
  }

}

