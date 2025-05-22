# Create Wrapper
gradle wrapper

# Update Wrapper - distribution-type (all/bin)
# 1. Call updates gradle-wrapper.properties.
# 2. Call updates gradle-wrapper.jar and gradlew.* Files.

./gradlew wrapper --distribution-type all --gradle-version x.y
./gradlew wrapper --distribution-type all --gradle-version latest
./gradlew wrapper --gradle-distribution-url https://...

# Using Wrapper
./gradlew TASK
