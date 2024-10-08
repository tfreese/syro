# Create Wrapper
gradle wrapper

# Update Wrapper - distribution-type (all/bin)
# 1. Call updates gradle-wrapper.properties.
# 2. Call updates gradle-wrapper.jar and gradlew.* Files.
./gradlew wrapper --gradle-version x.y      --distribution-type all
./gradlew wrapper --gradle-version latest   --distribution-type all
/gradlew wrapper  --gradle-distribution-url https://...

# Using Wrapper
./gradlew build
