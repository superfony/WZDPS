
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

public class UserInfoTo implements org.apache.thrift.TBase<UserInfoTo, UserInfoTo._Fields>, java.io.Serializable, Cloneable, Comparable<UserInfoTo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UserInfoTo");

  private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField DEPT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("deptId", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField DEPT_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("deptName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField GDJDM_FIELD_DESC = new org.apache.thrift.protocol.TField("gdjdm", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new UserInfoToStandardSchemeFactory());
    schemes.put(TupleScheme.class, new UserInfoToTupleSchemeFactory());
  }

  public String name; // required
  public int deptId; // required
  public String deptName; // required
  public String gdjdm; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NAME((short)1, "name"),
    DEPT_ID((short)2, "deptId"),
    DEPT_NAME((short)3, "deptName"),
    GDJDM((short)4, "gdjdm");

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
        case 1: // NAME
          return NAME;
        case 2: // DEPT_ID
          return DEPT_ID;
        case 3: // DEPT_NAME
          return DEPT_NAME;
        case 4: // GDJDM
          return GDJDM;
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
  private static final int __DEPTID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DEPT_ID, new org.apache.thrift.meta_data.FieldMetaData("deptId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.DEPT_NAME, new org.apache.thrift.meta_data.FieldMetaData("deptName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GDJDM, new org.apache.thrift.meta_data.FieldMetaData("gdjdm", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UserInfoTo.class, metaDataMap);
  }

  public UserInfoTo() {
  }

  public UserInfoTo(
    String name,
    int deptId,
    String deptName,
    String gdjdm)
  {
    this();
    this.name = name;
    this.deptId = deptId;
    setDeptIdIsSet(true);
    this.deptName = deptName;
    this.gdjdm = gdjdm;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UserInfoTo(UserInfoTo other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetName()) {
      this.name = other.name;
    }
    this.deptId = other.deptId;
    if (other.isSetDeptName()) {
      this.deptName = other.deptName;
    }
    if (other.isSetGdjdm()) {
      this.gdjdm = other.gdjdm;
    }
  }

  public UserInfoTo deepCopy() {
    return new UserInfoTo(this);
  }

  @Override
  public void clear() {
    this.name = null;
    setDeptIdIsSet(false);
    this.deptId = 0;
    this.deptName = null;
    this.gdjdm = null;
  }

  public String getName() {
    return this.name;
  }

  public UserInfoTo setName(String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been assigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public int getDeptId() {
    return this.deptId;
  }

  public UserInfoTo setDeptId(int deptId) {
    this.deptId = deptId;
    setDeptIdIsSet(true);
    return this;
  }

  public void unsetDeptId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __DEPTID_ISSET_ID);
  }

  /** Returns true if field deptId is set (has been assigned a value) and false otherwise */
  public boolean isSetDeptId() {
    return EncodingUtils.testBit(__isset_bitfield, __DEPTID_ISSET_ID);
  }

  public void setDeptIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __DEPTID_ISSET_ID, value);
  }

  public String getDeptName() {
    return this.deptName;
  }

  public UserInfoTo setDeptName(String deptName) {
    this.deptName = deptName;
    return this;
  }

  public void unsetDeptName() {
    this.deptName = null;
  }

  /** Returns true if field deptName is set (has been assigned a value) and false otherwise */
  public boolean isSetDeptName() {
    return this.deptName != null;
  }

  public void setDeptNameIsSet(boolean value) {
    if (!value) {
      this.deptName = null;
    }
  }

  public String getGdjdm() {
    return this.gdjdm;
  }

  public UserInfoTo setGdjdm(String gdjdm) {
    this.gdjdm = gdjdm;
    return this;
  }

  public void unsetGdjdm() {
    this.gdjdm = null;
  }

  /** Returns true if field gdjdm is set (has been assigned a value) and false otherwise */
  public boolean isSetGdjdm() {
    return this.gdjdm != null;
  }

  public void setGdjdmIsSet(boolean value) {
    if (!value) {
      this.gdjdm = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((String)value);
      }
      break;

    case DEPT_ID:
      if (value == null) {
        unsetDeptId();
      } else {
        setDeptId((Integer)value);
      }
      break;

    case DEPT_NAME:
      if (value == null) {
        unsetDeptName();
      } else {
        setDeptName((String)value);
      }
      break;

    case GDJDM:
      if (value == null) {
        unsetGdjdm();
      } else {
        setGdjdm((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case NAME:
      return getName();

    case DEPT_ID:
      return Integer.valueOf(getDeptId());

    case DEPT_NAME:
      return getDeptName();

    case GDJDM:
      return getGdjdm();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case NAME:
      return isSetName();
    case DEPT_ID:
      return isSetDeptId();
    case DEPT_NAME:
      return isSetDeptName();
    case GDJDM:
      return isSetGdjdm();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof UserInfoTo)
      return this.equals((UserInfoTo)that);
    return false;
  }

  public boolean equals(UserInfoTo that) {
    if (that == null)
      return false;

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_deptId = true;
    boolean that_present_deptId = true;
    if (this_present_deptId || that_present_deptId) {
      if (!(this_present_deptId && that_present_deptId))
        return false;
      if (this.deptId != that.deptId)
        return false;
    }

    boolean this_present_deptName = true && this.isSetDeptName();
    boolean that_present_deptName = true && that.isSetDeptName();
    if (this_present_deptName || that_present_deptName) {
      if (!(this_present_deptName && that_present_deptName))
        return false;
      if (!this.deptName.equals(that.deptName))
        return false;
    }

    boolean this_present_gdjdm = true && this.isSetGdjdm();
    boolean that_present_gdjdm = true && that.isSetGdjdm();
    if (this_present_gdjdm || that_present_gdjdm) {
      if (!(this_present_gdjdm && that_present_gdjdm))
        return false;
      if (!this.gdjdm.equals(that.gdjdm))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(UserInfoTo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetName()).compareTo(other.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, other.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDeptId()).compareTo(other.isSetDeptId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDeptId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.deptId, other.deptId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDeptName()).compareTo(other.isSetDeptName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDeptName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.deptName, other.deptName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGdjdm()).compareTo(other.isSetGdjdm());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGdjdm()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gdjdm, other.gdjdm);
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
    StringBuilder sb = new StringBuilder("UserInfoTo(");
    boolean first = true;

    sb.append("name:");
    if (this.name == null) {
      sb.append("null");
    } else {
      sb.append(this.name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("deptId:");
    sb.append(this.deptId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("deptName:");
    if (this.deptName == null) {
      sb.append("null");
    } else {
      sb.append(this.deptName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("gdjdm:");
    if (this.gdjdm == null) {
      sb.append("null");
    } else {
      sb.append(this.gdjdm);
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

  private static class UserInfoToStandardSchemeFactory implements SchemeFactory {
    public UserInfoToStandardScheme getScheme() {
      return new UserInfoToStandardScheme();
    }
  }

  private static class UserInfoToStandardScheme extends StandardScheme<UserInfoTo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UserInfoTo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.name = iprot.readString();
              struct.setNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // DEPT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.deptId = iprot.readI32();
              struct.setDeptIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DEPT_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.deptName = iprot.readString();
              struct.setDeptNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // GDJDM
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gdjdm = iprot.readString();
              struct.setGdjdmIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, UserInfoTo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.name != null) {
        oprot.writeFieldBegin(NAME_FIELD_DESC);
        oprot.writeString(struct.name);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(DEPT_ID_FIELD_DESC);
      oprot.writeI32(struct.deptId);
      oprot.writeFieldEnd();
      if (struct.deptName != null) {
        oprot.writeFieldBegin(DEPT_NAME_FIELD_DESC);
        oprot.writeString(struct.deptName);
        oprot.writeFieldEnd();
      }
      if (struct.gdjdm != null) {
        oprot.writeFieldBegin(GDJDM_FIELD_DESC);
        oprot.writeString(struct.gdjdm);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserInfoToTupleSchemeFactory implements SchemeFactory {
    public UserInfoToTupleScheme getScheme() {
      return new UserInfoToTupleScheme();
    }
  }

  private static class UserInfoToTupleScheme extends TupleScheme<UserInfoTo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UserInfoTo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetName()) {
        optionals.set(0);
      }
      if (struct.isSetDeptId()) {
        optionals.set(1);
      }
      if (struct.isSetDeptName()) {
        optionals.set(2);
      }
      if (struct.isSetGdjdm()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetName()) {
        oprot.writeString(struct.name);
      }
      if (struct.isSetDeptId()) {
        oprot.writeI32(struct.deptId);
      }
      if (struct.isSetDeptName()) {
        oprot.writeString(struct.deptName);
      }
      if (struct.isSetGdjdm()) {
        oprot.writeString(struct.gdjdm);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UserInfoTo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.name = iprot.readString();
        struct.setNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.deptId = iprot.readI32();
        struct.setDeptIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.deptName = iprot.readString();
        struct.setDeptNameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.gdjdm = iprot.readString();
        struct.setGdjdmIsSet(true);
      }
    }
  }

}

