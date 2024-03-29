package me.jchenry.delta.openapi.generator;

import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.AbstractGoCodegen;
import org.openapitools.codegen.meta.features.*;

// import org.openapitools.codegen.model.*;
// import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;

public class GoServerLambdaGenerator extends AbstractGoCodegen {

  // source folder where to write the files
  protected String sourceFolder = "pkg";
  protected String apiVersion = "1.0.0";
  protected String packageVersion = "1.0.0";

  /**
   * Configures the type of generator.
   *
   * @return the CodegenType for this generator
   * @see org.openapitools.codegen.CodegenType
   */
  public CodegenType getTag() {
    return CodegenType.SERVER;
  }

  /**
   * Configures a friendly name for the generator. This will be used by the
   * generator
   * to select the library with the -g flag.
   *
   * @return the friendly name for the generator
   */
  public String getName() {
    return "go-server-lambda";
  }

  /**
   * Returns human-friendly help for the generator. Provide the consumer with help
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
            SecurityFeature.class))
        .excludeGlobalFeatures(
            GlobalFeature.XMLStructureDefinitions,
            GlobalFeature.Callbacks,
            GlobalFeature.LinkObjects,
            GlobalFeature.ParameterStyling)
        .excludeSchemaSupportFeatures(
            SchemaSupportFeature.Polymorphism)
        .excludeParameterFeatures(
            ParameterFeature.Cookie));

    outputFolder = "generated-code/go-server-lambda";

    modelTemplateFiles.put("model.mustache",".go");
   
    apiTemplateFiles.put("api.mustache",".go"); 
    // apiTemplateFiles.put("api_test.mustache", "_test.go");
    apiTemplateFiles.put( "lambda.mustache", "_main.go"); // the extension for each file to write
    apiTemplateFiles.put( "operation.mustache", ".go"); // the extension for each file to write

    supportingFiles.add(new SupportingFile("utils.mustache","pkg/api","utils.go")); 
    supportingFiles.add(new SupportingFile("keep.mustache","pkg/operations","doc.go")); 

   // this overloads the function of API Documentation templates
   // to allow for a main entry point to be created

    templateDir = "go-server-lambda";

    apiPackage = "api";

    modelPackage = "model";

    setReservedWordsLowerCase(Arrays.asList(
        // data type
        "string", "bool", "uint", "uint8", "uint16", "uint32", "uint64",
        "int", "int8", "int16", "int32", "int64", "float32", "float64",
        "complex64", "complex128", "rune", "byte", "uintptr",

        "break", "default", "func", "interface", "select",
        "case", "defer", "go", "map", "struct",
        "chan", "else", "goto", "package", "switch",
        "const", "fallthrough", "if", "range", "type",
        "continue", "for", "import", "return", "var", "error", "nil"));
    additionalProperties.put("apiVersion", apiVersion);

  

    /**
     * Language Specific Primitives. These types will not trigger imports by
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
            "interface{}"));
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
    typeMapping.put("object", "map[string]interface{}");
    typeMapping.put("AnyType", "interface{}");

    importMapping = new HashMap<>();

    cliOptions.clear();
    cliOptions.add(new CliOption(CodegenConstants.PACKAGE_NAME, "Go package name (convention: lowercase).")
        .defaultValue("openapi"));
    cliOptions.add(new CliOption(CodegenConstants.PACKAGE_VERSION, "Go package version.")
        .defaultValue("1.0.0"));
    cliOptions
        .add(new CliOption(CodegenConstants.HIDE_GENERATION_TIMESTAMP, CodegenConstants.HIDE_GENERATION_TIMESTAMP_DESC)
            .defaultValue(Boolean.TRUE.toString()));

  }

  // This tags each operation with its own tag so it may be generated seperately. 
  @Override
  public void preprocessOpenAPI(OpenAPI openAPI) {

    Paths newPaths = new Paths();

    for (String path : openAPI.getPaths().keySet()) {
      PathItem config =  openAPI.getPaths().get(path);
      for (Operation operation : config.readOperations()){
        operation.setTags(Arrays.asList(operation.getOperationId()));
      }

      String wayPath = path.replace('{', ':').replace("}", "");
      newPaths.put(wayPath, config);
    }

    openAPI.setPaths(newPaths);
    super.preprocessOpenAPI(openAPI);
  }

  
  public String modelFileFolder() {
    return outputFolder + "/" + sourceFolder + "/" + modelPackage().replace('.', File.separatorChar);
  }

  @Override
  public String apiFilename(String templateName, String tag) {
      String suffix = apiTemplateFiles().get(templateName);
      if (templateName == "operation.mustache"){
        return outputFolder + File.separator+ "pkg" + File.separator + "operations" +File.separator+ toApiFilename(tag) + suffix;

      } else {
        return apiFileFolder() + File.separator + tag +File.separator+ toApiFilename(tag) + suffix;
      }
  }
  
  @Override
  public String apiFileFolder() {
    return CmdFolder();
  }

  private String CmdFolder(){
    return outputFolder + "/" + "cmd"; 
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
     * Additional Properties. These values can be passed to the templates and
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

  // supportingFiles.add(new SupportingFile("go.mod.mustache", "", "go.mod"));

}
