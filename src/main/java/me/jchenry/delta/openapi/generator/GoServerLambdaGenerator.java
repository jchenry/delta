package me.jchenry.delta.openapi.generator;

import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.AbstractGoCodegen;
import org.openapitools.codegen.meta.features.*;

// import org.openapitools.codegen.model.*;
// import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class GoServerLambdaGenerator extends AbstractGoCodegen {

  // source folder where to write the files
  protected String sourceFolder = "pkg";
  protected String apiVersion = "1.0.0";
  protected String packageVersion = "1.0.0";

  /**
   * Configures the type of generator.
   *
   * @return  the CodegenType for this generator
   * @see     org.openapitools.codegen.CodegenType
   */
  public CodegenType getTag() {
    return CodegenType.SERVER;
  }

  /**
   * Configures a friendly name for the generator.  This will be used by the generator
   * to select the library with the -g flag.
   *
   * @return the friendly name for the generator
   */
  public String getName() {
    return "go-server-lambda";
  }


  /**
   * Returns human-friendly help for the generator.  Provide the consumer with help
   * tips, parameters here
   *
   * @return A string value for the help message
   */
  public String getHelp() {
    return "Generates a go-server-lambda client library.";
  }

  public GoServerLambdaGenerator() {
    super();

    modifyFeatureSet(features -> features
    .includeDocumentationFeatures(DocumentationFeature.Readme)
    .wireFormatFeatures(EnumSet.of(WireFormatFeature.JSON, WireFormatFeature.XML))
    .securityFeatures(EnumSet.noneOf(
            SecurityFeature.class
    ))
    .excludeGlobalFeatures(
            GlobalFeature.XMLStructureDefinitions,
            GlobalFeature.Callbacks,
            GlobalFeature.LinkObjects,
            GlobalFeature.ParameterStyling
    )
    .excludeSchemaSupportFeatures(
            SchemaSupportFeature.Polymorphism
    )
    .excludeParameterFeatures(
            ParameterFeature.Cookie
    )
);


    // set the output folder here
    outputFolder = "generated-code/go-server-lambda";

    /**
     * Models.  You can write model files using the modelTemplateFiles map.
     * if you want to create one template for file, you can do so here.
     * for multiple files for model, just put another entry in the `modelTemplateFiles` with
     * a different extension
     */
    modelTemplateFiles.put(
      "model.mustache", // the template to use
      ".go");       // the extension for each file to write

    /**
     * Api classes.  You can write classes for each Api file with the apiTemplateFiles map.
     * as with models, add multiple entries with different extensions for multiple files per
     * class
     */
    apiTemplateFiles.put(
      "api.mustache",   // the template to use
      ".go");       // the extension for each file to write

      apiDocTemplateFiles.put(
        "lambda.mustache",   // the template to use
        ".go");       // the extension for each file to write
  


    /**
     * Template Location.  This is the location which templates will be read from.  The generator
     * will use the resource stream to attempt to read the templates.
     */
    templateDir = "go-server-lambda";

    // /**
    //  * Api Package.  Optional, if needed, this can be used in templates
    //  */
    apiPackage = "org.openapitools.api";

    // /**
    //  * Model Package.  Optional, if needed, this can be used in templates
    //  */
    modelPackage = "org.openapitools.model";

    /**
     * Reserved words.  Override this with reserved words specific to your language
     */
    setReservedWordsLowerCase(
      Arrays.asList(
              // data type
              "string", "bool", "uint", "uint8", "uint16", "uint32", "uint64",
              "int", "int8", "int16", "int32", "int64", "float32", "float64",
              "complex64", "complex128", "rune", "byte", "uintptr",

              "break", "default", "func", "interface", "select",
              "case", "defer", "go", "map", "struct",
              "chan", "else", "goto", "package", "switch",
              "const", "fallthrough", "if", "range", "type",
              "continue", "for", "import", "return", "var", "error", "nil")
      // Added "error" as it's used so frequently that it may as well be a keyword
);
    // /**
    //  * Additional Properties.  These values can be passed to the templates and
    //  * are available in models, apis, and supporting files
    //  */
    additionalProperties.put("apiVersion", apiVersion);

    /**
     * Supporting Files.  You can write single files for the generator with the
     * entire object tree available.  If the input file has a suffix of `.mustache
     * it will be processed by the template engine.  Otherwise, it will be copied
     */
    supportingFiles.add(new SupportingFile("myFile.mustache",   // the input template or file
      "",                                                       // the destination folder, relative `outputFolder`
      "myFile.sample")                                          // the output file
    );

    /**
     * Language Specific Primitives.  These types will not trigger imports by
     * the client generator
     */
    languageSpecificPrimitives = new HashSet<>(
      Arrays.asList(
              "string",
              "bool",
              "uint",
              "uint32",
              "uint64",
              "int",
              "int32",
              "int64",
              "float32",
              "float64",
              "complex64",
              "complex128",
              "rune",
              "byte",
              "map[string]interface{}",
              "interface{}"
      )
);
        typeMapping.clear();
        typeMapping.put("integer", "int32");
        typeMapping.put("long", "int64");
        typeMapping.put("number", "float32");
        typeMapping.put("float", "float32");
        typeMapping.put("double", "float64");
        typeMapping.put("decimal", "float64");
        typeMapping.put("boolean", "bool");
        typeMapping.put("string", "string");
        typeMapping.put("UUID", "string");
        typeMapping.put("URI", "string");
        typeMapping.put("date", "string");
        typeMapping.put("DateTime", "time.Time");
        typeMapping.put("password", "string");
        typeMapping.put("File", "*os.File");
        typeMapping.put("file", "*os.File");
        typeMapping.put("binary", "*os.File");
        typeMapping.put("ByteArray", "string");
        typeMapping.put("null", "nil");
        // A 'type: object' OAS schema without any declared property is
        // (per JSON schema specification) "an unordered set of properties
        // mapping a string to an instance".
        // Hence map[string]interface{} is the proper implementation in golang.
        // Note: OpenAPITools uses the same token 'object' for free-form objects
        // and arbitrary types. A free form object is implemented in golang as
        // map[string]interface{}, whereas an arbitrary type is implemented
        // in golang as interface{}.
        // See issue #5387 for more details.
        typeMapping.put("object", "map[string]interface{}");
        typeMapping.put("AnyType", "interface{}");

        importMapping = new HashMap<>();

        cliOptions.clear();
        cliOptions.add(new CliOption(CodegenConstants.PACKAGE_NAME, "Go package name (convention: lowercase).")
                .defaultValue("openapi"));
        cliOptions.add(new CliOption(CodegenConstants.PACKAGE_VERSION, "Go package version.")
                .defaultValue("1.0.0"));
        cliOptions.add(new CliOption(CodegenConstants.HIDE_GENERATION_TIMESTAMP, CodegenConstants.HIDE_GENERATION_TIMESTAMP_DESC)
                .defaultValue(Boolean.TRUE.toString()));

  }


  /**
   * Location to write model files.  You can use the modelPackage() as defined when the class is
   * instantiated
   */
  public String modelFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + modelPackage().replace('.', File.separatorChar);
  }

  /**
   * Location to write api files.  You can use the apiPackage() as defined when the class is
   * instantiated
   */
  @Override
  public String apiFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + apiPackage().replace('.', File.separatorChar);
  }

  @Override
  public String apiDocFilename(String templateName, String tag) {
      String docExtension = getDocExtension();
      String suffix = docExtension != null ? docExtension : apiDocTemplateFiles().get(templateName);
      return outputFolder + File.separator + "cmd" + File.separator + toApiDocFilename(tag) + File.separator+"main" + suffix;
  }

  


  @Override
  public String apiDocFileFolder() {
      return outputFolder + "/"+"cmd";
  }


  public void setSourceFolder(String sourceFolder) {
    this.sourceFolder = sourceFolder;
}

public void setPackageVersion(String packageVersion) {
    this.packageVersion = packageVersion;
}


  @Override
  public void processOpts() {
      super.processOpts();
      /*
       * Additional Properties.  These values can be passed to the templates and
       * are available in models, apis, and supporting files
       */
      if (additionalProperties.containsKey(CodegenConstants.PACKAGE_NAME)) {
          setPackageName((String) additionalProperties.get(CodegenConstants.PACKAGE_NAME));
      } else {
          setPackageName("openapi");
          additionalProperties.put(CodegenConstants.PACKAGE_NAME, packageName);
      }

      if (additionalProperties.containsKey(CodegenConstants.PACKAGE_VERSION)) {
          this.setPackageVersion((String) additionalProperties.get(CodegenConstants.PACKAGE_VERSION));
      } else {
          additionalProperties.put(CodegenConstants.PACKAGE_VERSION, packageVersion);
      }

      if (additionalProperties.containsKey(CodegenConstants.SOURCE_FOLDER)) {
          this.setSourceFolder((String) additionalProperties.get(CodegenConstants.SOURCE_FOLDER));
      } else {
          additionalProperties.put(CodegenConstants.SOURCE_FOLDER, sourceFolder);
      }
    }

      // supportingFiles.add(new SupportingFile("main.mustache", "", "main.go"));
      // supportingFiles.add(new SupportingFile("go.mod.mustache", "", "go.mod"));



  }
