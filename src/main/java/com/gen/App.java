package com.gen;

import com.gen.constants.SysConstant;
import com.gen.helper.AsciidoctorHelper;
import com.gen.helper.DirectoryHelper;
import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.apache.commons.cli.*;

import java.net.URL;
import java.nio.file.Paths;


/**
 * gen html doc
 *
 */
public class App 
{
    private static final String SWAGGER_API = "http://localhost:8080/demo/v2/api-docs";

    private static final String SWAGGER_JSON = "./docs/swagger/swagger.json";

    private static final String ASCIIDOC_FILE_PATH = "./docs/asciidoc/all.adoc";

    public static void main( String[] args ) throws Exception
    {
        Options options = new Options();
        options.addOption("url", true, "swagger api url");
        options.addOption("o", true, "api document output path");
        options.addOption("h", false, "help");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,args);

        String swaggerApi = SWAGGER_API;
        if (cmd.hasOption("url")){
            swaggerApi =cmd.getOptionValue("url");
        }

        String output = SysConstant.OUTPUT_HTML_DIR;
        if (cmd.hasOption("o")){
            output =cmd.getOptionValue("o");
        }

        if (cmd.hasOption("h")){
            System.out.println("帮助：\n[-url] swagger api url\n[-o] api document output path\n[-h] help");
            System.exit(0);
        }
        generateAsciiDocsToFile(swaggerApi);
        AsciidoctorHelper.generateAsciiDocsToHtml(ASCIIDOC_FILE_PATH,output);

        clearAsciiDoc(output.equals(SysConstant.OUTPUT_HTML_DIR));
    }

    private static void clearAsciiDoc(boolean isDefault)
    {
        if(isDefault)
        {
            DirectoryHelper.deleteDir("./docs/asciidoc");
        }
        else
        {
            DirectoryHelper.deleteDir("./docs");
        }
    }

    private static void generateAsciiDocsToFile(String swaggerApi) throws Exception {
        System.out.println("start generate asciiDoc...");
        //    输出Ascii到单文件
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .build();

        Swagger2MarkupConverter.from(new URL(swaggerApi))
                .withConfig(config)
                .build()
                .toFile(Paths.get("./docs/asciidoc/all"));
        System.out.println("finished generate asciiDoc...");
    }

    private static void generateMarkdownDocsToFile(String swaggerApi) throws Exception {
        //    输出Markdown到单文件
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .build();

        Swagger2MarkupConverter.from(new URL(swaggerApi))
                .withConfig(config)
                .build()
                .toFile(Paths.get("./docs/markdown/all"));
    }
}
