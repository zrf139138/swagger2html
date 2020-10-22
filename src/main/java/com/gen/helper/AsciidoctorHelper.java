package com.gen.helper;

/**
 * @author Zero
 * @date 2020-10-22 10:49.
 */

import com.gen.constants.SysConstant;
import org.asciidoctor.*;

import java.io.File;
import java.util.*;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.AttributesBuilder.attributes;

/**
 * Utility class for re-usable logic.
 */
public class AsciidoctorHelper {

	//创建Asciidoctor
	private static Asciidoctor asciidoctor = create();

	public  static String generateAsciiDocsToString(String filePath)
	{
		//生成AsciiDoc String
		return asciidoctor.convertFile(
				new File(filePath),
				new HashMap<String, Object>());
	}

	public  static void generateAsciiDocsToHtml(String filePath)
	{
		generateAsciiDocsToHtml(filePath,SysConstant.OUTPUT_HTML_DIR);
	}

	public  static void generateAsciiDocsToHtml(String filePath,String out)
	{
		System.out.println("start generate Html...");
		AttributesBuilder attributesBuilder = attributes();
		setAttributesOnBuilder(attributesBuilder);

		OptionsBuilder optionsBuilder =OptionsBuilder.options();
		setOptionsOnBuilder(optionsBuilder,out);

		optionsBuilder.attributes(attributesBuilder);
		//生成AsciiDoc String
		 asciidoctor.convertFile(new File(filePath),optionsBuilder);

		System.out.println("finished generate Html...");
	}

	public  static void generateAsciiDocsToHtmls(List<String> filePath)
	{
		generateAsciiDocsToHtmls(filePath,SysConstant.OUTPUT_HTML_DIR);
	}

	public  static void generateAsciiDocsToHtmls(List<String> filePath,String out)
	{
		AttributesBuilder attributesBuilder = attributes();
		setAttributesOnBuilder(attributesBuilder);

		OptionsBuilder optionsBuilder =OptionsBuilder.options();
		setOptionsOnBuilder(optionsBuilder,out);

		optionsBuilder.attributes(attributesBuilder);

		List<File> files =new ArrayList<>();

		filePath.forEach(x->files.add(new File(x)));
		asciidoctor.convertFiles(files, optionsBuilder);
	}

	public  static void generateAsciiDocsToHtmls(String dirPath)
	{
		generateAsciiDocsToHtmls(dirPath,SysConstant.OUTPUT_HTML_DIR);
	}

	public  static void generateAsciiDocsToHtmls(String dirPath,String out)
	{
		AttributesBuilder attributesBuilder = attributes();
		setAttributesOnBuilder(attributesBuilder);

		OptionsBuilder optionsBuilder =OptionsBuilder.options();
		setOptionsOnBuilder(optionsBuilder,out);

		optionsBuilder.attributes(attributesBuilder);

		//文件夹下文件转换
		asciidoctor.convertDirectory(
				new AsciiDocDirectoryWalker(dirPath),
				optionsBuilder);
	}

	/**
	 * 可选builder
	 * @param optionsBuilder
	 */
	private static void setOptionsOnBuilder(OptionsBuilder optionsBuilder,String out) {
		optionsBuilder
				//生成HTML
				.backend("html")
				.safe(SafeMode.UNSAFE)
				.headerFooter(true)
				.mkDirs(true)
				.docType("book")
//				//输出路径
//				.toDir(new File(SysConstant.OUTPUT_HTML_DIR))
//				//目标路径
//				.destinationDir(new File(SysConstant.OUTPUT_HTML_DIR))
				.toFile(new File(out + "doc.html"));
	}
	/**
	 * 可选builder
	 * @param optionsBuilder
	 */
	private static void setOptionsOnBuilder(OptionsBuilder optionsBuilder) {
		setOptionsOnBuilder(optionsBuilder,SysConstant.OUTPUT_HTML_DIR);
	}

	/**
	 * 可选属性
	 * @param attributesBuilder
	 */
	private static void setAttributesOnBuilder(AttributesBuilder attributesBuilder) {
		attributesBuilder.title("api文档")
				.allowUriRead(true)
				.tableOfContents(true)
				.attribute("toc" , "left")
				.sectionNumbers(true)
				.sourceHighlighter("coderay")
				.imagesDir("images@")
				.attributeMissing("skip")
				.attributeUndefined("drop-line");
	}
	/**
	 * Adds attributes from a {@link Map} into a {@link AttributesBuilder} taking care of Maven's XML parsing special
	 * cases like toggles, nulls, etc.
	 *
	 * @param attributes        map of Asciidoctor attributes
	 * @param attributesBuilder AsciidoctorJ AttributesBuilder
	 */
	public static void addAttributes(final Map<String, Object> attributes, AttributesBuilder attributesBuilder) {
		// TODO Figure out how to reliably set other values (like boolean values, dates, times, etc)
		for (Map.Entry<String, Object> attributeEntry : attributes.entrySet()) {
			addAttribute(attributeEntry.getKey(), attributeEntry.getValue(), attributesBuilder);
		}
	}

	/**
	 * Adds an attribute into a {@link AttributesBuilder} taking care of Maven's XML parsing special cases like
	 * toggles toggles, nulls, etc.
	 *
	 * @param attribute         Asciidoctor attribute name
	 * @param value             Asciidoctor attribute value
	 * @param attributesBuilder AsciidoctorJ AttributesBuilder
	 */
	public static void addAttribute(String attribute, Object value, AttributesBuilder attributesBuilder) {
		// NOTE Maven interprets an empty value as null, so we need to explicitly convert it to empty string (see #36)
		// NOTE In Asciidoctor, an empty string represents a true value
		if (value == null || "true".equals(value)) {
			attributesBuilder.attribute(attribute, "");
		}
		// NOTE a value of false is effectively the same as a null value, so recommend the use of the string "false"
		else if ("false".equals(value)) {
			attributesBuilder.attribute(attribute, null);
		}
		// NOTE Maven can't assign a Boolean value from the XML-based configuration, but a client may
		else if (value instanceof Boolean) {
			attributesBuilder.attribute(attribute, Attributes.toAsciidoctorFlag((Boolean) value));
		} else {
			// Can't do anything about dates and times because all that logic is private in Attributes
			attributesBuilder.attribute(attribute, value);
		}
	}
}