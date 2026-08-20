
set -e

echo "Cleaning bin/"
rm -rf bin
mkdir -p bin

echo "Compiling Java sources..."
javac -d bin $(find src -name "*.java")

echo "Copying image resources..."
cp -r src/images bin/

echo "Done! Run your program using:"
echo "java -cp bin com.whackamole.Main"
