package time.domain;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.util.NumericUtils;

/**
 * Copie de LongField mais avec 
 * 
 * @author slim
 *
 */
public final class SortableLongField extends Field {
  
  /** 
   * Type for a LongField that is not stored:
   * normalization factors, frequencies, and positions are omitted.
   */
  public static final FieldType TYPE_NOT_STORED = new FieldType();
  static {
    TYPE_NOT_STORED.setTokenized(true);
    TYPE_NOT_STORED.setOmitNorms(true);
    TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
    TYPE_NOT_STORED.setNumericType(FieldType.NumericType.LONG);
    TYPE_NOT_STORED.setDocValuesType(DocValuesType.NUMERIC);
    TYPE_NOT_STORED.freeze();
  }

  /** 
   * Type for a stored LongField:
   * normalization factors, frequencies, and positions are omitted.
   */
  public static final FieldType TYPE_STORED = new FieldType();
  static {
    TYPE_STORED.setTokenized(true);
    TYPE_STORED.setOmitNorms(true);
    TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
    TYPE_STORED.setNumericType(FieldType.NumericType.LONG);
    TYPE_STORED.setStored(true);
    TYPE_STORED.setDocValuesType(DocValuesType.NUMERIC);
    TYPE_STORED.freeze();
  }

  /** Creates a stored or un-stored LongField with the provided value
   *  and default <code>precisionStep</code> {@link
   *  NumericUtils#PRECISION_STEP_DEFAULT} (16). 
   *  @param name field name
   *  @param value 64-bit long value
   *  @param stored Store.YES if the content should also be stored
   *  @throws IllegalArgumentException if the field name is null.
   */
  public SortableLongField(String name, long value, Store stored) {
    super(name, stored == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
    fieldsData = Long.valueOf(value);
  }
  
  /** Expert: allows you to customize the {@link
   *  FieldType}. 
   *  @param name field name
   *  @param value 64-bit long value
   *  @param type customized field type: must have {@link FieldType#numericType()}
   *         of {@link FieldType.NumericType#LONG}.
   *  @throws IllegalArgumentException if the field name or type is null, or
   *          if the field type does not have a LONG numericType()
   */
  public SortableLongField(String name, long value, FieldType type) {
    super(name, type);
    if (type.numericType() != FieldType.NumericType.LONG) {
      throw new IllegalArgumentException("type.numericType() must be LONG but got " + type.numericType());
    }
    fieldsData = Long.valueOf(value);
  }
}
