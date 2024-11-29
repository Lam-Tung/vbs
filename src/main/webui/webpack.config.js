const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const DuplicatePackageCheckerPlugin = require('duplicate-package-checker-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const project = require('./aurelia_project/aurelia.json');
const { AureliaPlugin } = require('aurelia-webpack-plugin');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

// config helpers:
const ensureArray = (config) => config && (Array.isArray(config) ? config : [config]) || [];
const when = (condition, config, negativeConfig) =>
  condition ? ensureArray(config) : ensureArray(negativeConfig);

// primary config:
const outDir = path.resolve(__dirname, project.platform.output);
const srcDir = path.resolve(__dirname, 'src');
const baseUrl = '/';

const cssRules = [
  {
    loader: 'css-loader'
  }
];

module.exports = ({ production }, { analyze, hmr, port, host }) => ({
  resolve: {
    extensions: ['.ts', '.js', '.scss'], // Add .scss extension
    modules: [srcDir, 'node_modules'],
    alias: {
      'aurelia-binding': path.resolve(__dirname, 'node_modules/aurelia-binding')
    }
  },
  entry: {
    app: [
      'aurelia-bootstrapper'
    ]
  },
  mode: production ? 'production' : 'development',
  output: {
    path: outDir,
    publicPath: baseUrl,
    filename: production ? '[name].[chunkhash].bundle.js' : '[name].[fullhash].bundle.js',
    chunkFilename: production ? '[name].[chunkhash].chunk.js' : '[name].[fullhash].chunk.js'
  },
  optimization: {
    minimizer: [
      new TerserPlugin({
        terserOptions: {
          compress: false
        },
      }),
    ],
    runtimeChunk: true,
    moduleIds: 'deterministic',
    splitChunks: {
      hidePathInfo: true,
      chunks: "initial",
      maxSize: 200000,
      cacheGroups: {
        vendors: {
          test: /[\\/]node_modules[\\/]/,
          name: 'vendors',
          priority: 19,
          enforce: true,
          minSize: 30000
        }
      }
    }
  },
  performance: { hints: false },
  devServer: {
    historyApiFallback: true,
    open: project.platform.open,
    hot: hmr || project.platform.hmr,
    port: port || project.platform.port,
    host: host
  },
  devtool: production ? undefined : 'cheap-module-source-map',
  module: {
    rules: [
      // Handle SCSS files
      {
        test: /\.scss$/i,
        issuer: { not: [ /\.html$/i ] },
        use: [
          { loader: MiniCssExtractPlugin.loader },
          'css-loader', // Handle CSS
          'sass-loader' // Compile SCSS to CSS
        ]
      },
      {
        test: /\.scss$/i,
        issuer: /\.html$/i,
        use: [
          'css-loader',
          'sass-loader' // Compile SCSS to CSS
        ]
      },
      { test: /\.html$/i, loader: 'html-loader', options: { minimize: false } },
      { test: /\.ts$/, loader: "ts-loader" },
      { test: /\.(png|svg|jpg|jpeg|gif)$/i, type: 'asset' },
      { test: /\.(woff|woff2|ttf|eot|svg|otf)(\?v=[0-9]\.[0-9]\.[0-9])?$/i, type: 'asset' },
      { test: /environment\.json$/i, use: [
        { loader: "app-settings-loader", options: { env: production ? 'production' : 'development' }},
      ]}
    ]
  },
  plugins: [
    new DuplicatePackageCheckerPlugin(),
    new AureliaPlugin(),
    new HtmlWebpackPlugin({
      template: 'index.ejs',
      metadata: {
        baseUrl
      }
    }),
    new MiniCssExtractPlugin({
      filename: production ? '[name].[contenthash].bundle.css' : '[name].[fullhash].bundle.css',
      chunkFilename: production ? '[name].[contenthash].chunk.css' : '[name].[fullhash].chunk.css'
    }),
    new CopyWebpackPlugin({
      patterns: [
        { from: 'static', to: outDir, globOptions: { ignore: ['.*'] } }
      ]
    }),
    ...when(analyze, new BundleAnalyzerPlugin()),
    new CleanWebpackPlugin()
  ]
});
