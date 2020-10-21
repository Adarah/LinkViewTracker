if [ $# -lt 1 ]; then
    echo "Not enough arguments, please provide the app's name";
    exit 2;
fi
# creates the project
npx create-next-app $1
cd $1

# configures typescript integration
touch tsconfig.json
npm install --save-dev typescript @types/react @types/node
npm install --save-dev eslint eslint-config-airbnb-typescript eslint-plugin-import eslint-plugin-jsx-a11y eslint-plugin-react eslint-plugin-react-hooks @typescript-eslint/eslint-plugin@latest
echo "module.exports = {
  extends: ['airbnb-typescript'],
  parserOptions: {
    project: './tsconfig.json',
  }
}" > .eslintrc.js

# organizes source files under src
mkdir -p src/components
mv -t src pages styles
rename .js .tsx src/**/*.js

npm audit fix
printf "\n\n\n\n\n"
printf "\nDon't forget to add baseUrl to the compilerOptions in tsconfig.json if you're using vscode! Otherwise imports might not work\n"
printf "\n\n\n\n\n"
npm run dev
