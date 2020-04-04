# keep everything in this package from being removed or renamed
-keep class com.arunkumar.dailynews.model.** { *; }

# keep everything in this package from being renamed only
-keepnames class com.arunkumar.dailynews.model.** { *; }

#---------- Gson start ----------

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

#---------- Gson end ----------

-keep class org.ocpsoft.prettytime.i18n.**