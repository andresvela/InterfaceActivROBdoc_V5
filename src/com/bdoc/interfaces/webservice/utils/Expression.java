package com.bdoc.interfaces.webservice.utils;

// modification : EDR - 6/7/2012 
// Ajout des substring simple ({index$'##'}) ou ## est un entier relatif permettant de prendre les n premier caracteres ou les -n derniers.
// 




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bdoc.interfaces.webservice.parametres.XMLIndexes;


public abstract class Expression implements ExpressionConstants {
	
	private static Map<String,Expression> expressions = new HashMap<String,Expression>();
	
	
	public static synchronized Expression get(String exp) {

		Expression expression = expressions.get(exp);
		if(expression == null) {
			expression = getPhrase(new OffsetString(exp));
			//TODO attention si config reload >> vider ce cache
			expressions.put(exp,expression);
		}
		return expression;
	}
	
	private static Expression getPhrase(OffsetString exp) {
		
		Phrase phrase = new Phrase();
		for(;;) {
			String part = exp.getUntil(TAG_BEGIN,false,false);
			if(part.length() > 0) {
				phrase.append(new ConstantOperand(part));
			
			} else if(exp.remains()) {
				phrase.append(getExpression(exp));
			
			} else {
				break;
			}
		}
		
		return phrase.shrink();
	}

	private static Expression getExpression(OffsetString exp) {

		if(exp.startsWith(TAG_BEGIN,true) == false) {
			throw new RuntimeException(exp.toString());
		}
		
		
		Expression operand = null;
		Operation operation = null;
		Operation extra = null;
		
		while(exp.remains()) {
			
			if(exp.startsWith(TAG_BLANK,true)) {
				
			} else if(exp.startsWith(TAG_END,true)) {
				break;
				
			} else if(exp.startsWith(TAG_BEGIN,false)) {
				Expression op = getExpression(exp);
				operand = (operation == null ? op : operation.append(op));
				operation = null;
			
			} else if(exp.startsWith(TAG_NOT_EQUAL,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new NotEqualOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_LIKE,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new LikeOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_EQUAL,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new EqualOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_AND,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new AndOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_OR,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new OrOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_ADD,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new AddOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_CHOICE_SELECT,true)) {
				if(operand == null) throw new RuntimeException(exp.toString());
				
				operation = new ChoiceOperation().append(operand);
				operand = null;
			
			} else if(exp.startsWith(TAG_CHOICE_SEPARTOR,true)) {
				if(operand instanceof ChoiceOperation == false) throw new RuntimeException(exp.toString());
				
				operation = (ChoiceOperation) operand;
				
			} else if(exp.startsWith(TAG_NOT,true)) {
				
				extra = new NotOperation();
				
			} else if(exp.startsWith(TAG_SUBSTRING,true)) {
				
				operation = new SubStringOperation().append(operand);
				operand = null;
			} else {
				
				if(exp.startsWith(LITERAL_DELIMITER,true)) {
					
					operand =  new ConstantOperand(exp.getUntil(LITERAL_DELIMITER,false,true));
					
				} else {
					
					String name = exp.getUntil("\\W+",true,false);
					
					String v = XMLIndexes.getinstance().getConstantIndexes().get(name);
					if(v != null) {
						operand = new ConstantOperand(v);
					} else {
						String e = XMLIndexes.getinstance().getComputedIndexes().get(name);
						if(e != null) {
							operand = Expression.get(e);
						} else {
							operand =  new VariableOperand(name);
						}
					}
				}

				if(extra != null) {
					operand = extra.append(operand);
					extra = null;
				}
				if(operation != null) {
					operand = operation.append(operand);
					operation = null;
				}
			}
		}
		
		return operand.shrink();
	}
	
	
	public abstract String getValue(ExpressionValues... values);
	
	public boolean getBoolean(ExpressionValues... values) {
		String value = getValue(values);
		if(value.equals("FALSE")) return false;
		if(value.equals("TRUE")) return true;
		throw new RuntimeException("invalid value : "+value);
	}
	
	protected abstract Expression shrink();
}

class OffsetString  {
	
	String string;
	int offset;
	
	public OffsetString(String s) {
		if(s == null) s = "";
		string = s;
	}
	public boolean startsWith(String s,boolean consume) {
		boolean b = string.startsWith(s,offset);
		if(b && consume) offset += s.length();
		return b;
	}
	public String getUntil(String s,boolean regexp,boolean consume) {
		String r;
		
		int i;
		if(regexp) {
			Matcher matcher = Pattern.compile(s).matcher(string);
			i = matcher.find(offset) ? matcher.start() : -1;
		} else {
			i = string.indexOf(s,offset);
		}
		
		if(i == -1) {
			r = string.substring(offset);
			offset = string.length();
		} else {
			r = string.substring(offset,i);
			offset = i;
		}
		
		if(consume) offset += s.length();
		
		return r;
	}
	public boolean remains() {
		return offset < string.length();
	}
	public String toString() {
		return "exp="+string+" offset="+offset+" view="+string.substring(offset);
	}
}

class Phrase extends Expression {
	
	private List<Expression> operands = new ArrayList<Expression>(3);
	
	public void append(Expression operand) {
		operands.add(operand.shrink());
	}
	public String getValue(ExpressionValues... values) {
		StringBuilder builder = new StringBuilder();
		for(Expression e : operands) builder.append(e.getValue(values));
		return builder.toString();
	}
	public Expression shrink() {
		if(operands.size() == 0) return new ConstantOperand("");
		if(operands.size() == 1) return operands.get(0);
		return this;
	}
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(Expression e : operands) builder.append(e);
		return builder.toString();
	}
}

class VariableOperand extends Expression {
	
	private String name;
	
	public VariableOperand(String v) {
		name = v;
	}
	public String getValue(ExpressionValues... values) {
		for(ExpressionValues iv : values) {
			String v = iv.get(name);
			if(v.length() > 0) return v;
		}
		return "";
	}
	public Expression shrink() {
		return this;
	}
	public String toString() {
		return name;
	}
}

class ConstantOperand extends Expression {
	
	private String value;
	
	public ConstantOperand(String v) {
		value = v;
	}
	public String getValue(ExpressionValues... values) {
		return value;
	}
	public Expression shrink() {
		return this;
	}
	public String toString() {
		return "'"+value+"'";
	}
}

abstract class Operation extends Expression {
	protected List<Expression> operands = new ArrayList<Expression>(3);
	protected int i;
	
	public Operation append(Expression e) {
		operands.add(e.shrink());
		return this;
	}
	public Expression first() {
		return operands.get(i=0);
	}
	public Expression next() {
		return operands.get(++i);
	}
	public boolean hasNext() {
		return i<operands.size();
	}
	public String first(ExpressionValues[] values) {
		return first().getValue(values);
	}
	public String next(ExpressionValues[] values) {
		return next().getValue(values);
	}
	public Expression shrink() {
		for(Expression e : operands) {
			if(e instanceof ConstantOperand == false) return this;
		}
		return new ConstantOperand(getValue());
	}
}

class EqualOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		String f = first(values);
		String n = next(values);
		return f.equals(n) ? "TRUE" : "FALSE";
	}
	public String toString() {
		return TAG_BEGIN+first()+" = "+next()+TAG_END;
	}
}

class LikeOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		String l = first().getValue(values);
		String r = next().getValue(values);
		r = r.replace(".","\\.").replace("*",".*");
		return l.matches(r) ? "TRUE" : "FALSE";
	}
	public String toString() {
		return TAG_BEGIN+first()+" LIKE "+next()+TAG_END;
	}
}

class NotEqualOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		String l = first().getValue(values);
		String r = next().getValue(values);
		return l.equals(r) ? "FALSE" : "TRUE";
	}
	public String toString() {
		return TAG_BEGIN+first()+" != "+next()+TAG_END;
	}
}
class AndOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		if(first().getValue(values) == "FALSE") return "FALSE";
		if(next().getValue(values) == "FALSE") return "FALSE";
		return "TRUE";
	}
	public String toString() {
		return TAG_BEGIN+first()+" AND "+next()+TAG_END;
	}
}
class OrOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		if(first().getValue(values) == "TRUE") return "TRUE";
		if(next().getValue(values) == "TRUE") return "TRUE";
		return "FALSE";
	}
	public String toString() {
		return TAG_BEGIN+first()+" OR "+next()+TAG_END;
	}
}
class ChoiceOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		String a = first(values);
		String b = next(values);
		String c = next(values);
		return a == "TRUE" ? b : c;
	}
	public String toString() {
		return TAG_BEGIN+first()+" ? "+next()+" : "+next()+TAG_END;
	}
}
class NotOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		return first(values) == "TRUE" ? "FALSE" : "TRUE";
	}
	public String toString() {
		return "!"+first();
	}
}

class AddOperation extends Operation {
	public String getValue(ExpressionValues... values) {
		String l = first().getValue(values);
		String r = next().getValue(values);
		return l+r;
	}
	public String toString() {
		return TAG_BEGIN+first()+" + "+next()+TAG_END;
	}
}

class SubStringOperation extends Operation {
	public String getValue(ExpressionValues... values) {
	String val=""+first().getValue(values);
	String startend=""+next().getValue(values);
	int start =0;
	int end = 0;
	int posVirgule;
	if ( (posVirgule = startend.indexOf(',')) >0)
	{
	start = Integer.parseInt(startend.substring(0,posVirgule));
	end   = Integer.parseInt(startend.substring(posVirgule+1,startend.length()));
	if (end<0) {start += end; end = start - end;}
	if (start<0){start=0;}
	if (end > val.length()) end = val.length();
	}
	else
	{
	start = 0;
	end   = Integer.parseInt(startend);
	if (end<0)
	{
	start = val.length()+end;
	end = val.length();
	}			
	}
	
	if (val == null || "".equals(val)) return "";
	
	return val.substring(start,end);

	}
	public String toString() {
	return TAG_BEGIN+first()+TAG_END;
	}
	}





interface ExpressionConstants {
	public static final String TAG_BLANK = " ";
	public static final String TAG_BEGIN = "{";
	public static final String TAG_END = "}";
	public static final String TAG_EQUAL = "=";
	public static final String TAG_NOT_EQUAL = "!=";
	public static final String TAG_LIKE = "LIKE";
	public static final String TAG_AND = "AND";
	public static final String TAG_OR = "OR";
	public static final String TAG_ADD = "+";
	public static final String TAG_CHOICE_SELECT = "?";
	public static final String TAG_CHOICE_SEPARTOR = ":";
	public static final String TAG_NOT = "!";
	public static final String TAG_SUBSTRING = "$";
	
	public static final String LITERAL_DELIMITER = "'";
}
