# Crypto Trade Risk Calculator

A comprehensive risk management tool for cryptocurrency trading that helps traders size their positions based on risk percentage rather than emotions. Available as both a web application and Android mobile app.

🌐 **Live Demo**: [https://jasurbek97.github.io/risk-calculator/](https://jasurbek97.github.io/risk-calculator/)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Web Application](#web-application)
  - [Android Application](#android-application)
- [How to Use](#how-to-use)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Crypto Trade Risk Calculator is designed to help traders make informed decisions by calculating position sizes based on predefined risk parameters. Instead of gambling with arbitrary amounts, this tool ensures you never risk more than you can afford to lose.

### Key Benefits

- **Risk-First Approach**: Size positions based on risk percentage, not emotions
- **Automatic Stop-Loss Calculation**: Calculates optimal stop-loss levels based on your risk tolerance
- **Multiple Take-Profit Levels**: Plan your exit strategy with multiple profit targets
- **Position Size Optimization**: Determines exact position sizes for your capital
- **Cross-Platform**: Available on web and Android devices

## ✨ Features

### 🔢 Risk Calculation
- **Capital Management**: Set your trading capital in USDT
- **Risk Distribution**: Define risk percentage (e.g., 1-5% of capital)
- **Position Sizing**: Automatic calculation of position sizes based on risk
- **Stop-Loss Automation**: Auto-calculated stop-loss levels

### 📊 Take-Profit Planning
- **Multiple TP Levels**: Set multiple take-profit targets
- **Risk Distribution**: Distribute risk across different TP levels
- **Profit Calculations**: See potential profits for each level
- **Portfolio Impact**: Understand how each trade affects your total capital

### 📱 User Experience
- **Responsive Design**: Works perfectly on desktop, tablet, and mobile
- **Real-time Calculations**: Instant updates as you modify parameters
- **Data Export**: Download calculations as SVG for record-keeping
- **Reset Functionality**: Quick reset to default values

### ⚠️ Risk Management
- **Risk Warnings**: Visual alerts when risk levels are too high
- **Conservative Defaults**: Starts with safe 3% risk distribution
- **Capital Protection**: Prevents over-leveraging and excessive risk

## 🚀 Getting Started

### Web Application

#### Prerequisites
- Modern web browser (Chrome, Firefox, Safari, Edge)
- Internet connection (for live version)

#### Quick Start
1. **Online**: Visit [https://jasurbek97.github.io/risk-calculator/](https://jasurbek97.github.io/risk-calculator/)
2. **Local**: Download `index.html` and open in your browser

#### Local Development
```bash
# Clone the repository
git clone https://github.com/yourusername/risk-calculator.git

# Navigate to project directory
cd risk-calculator

# Open the web app
open index.html
# or
python -m http.server 8000  # Then visit http://localhost:8000
```

### Android Application

#### Prerequisites
- Android device with API level 21+ (Android 5.0+)
- APK installation permissions enabled

#### Installation
1. Download the latest APK from the `AndroidApp/app/release/` directory
2. Install the APK on your Android device
3. Launch "Risk Calculator" from your app drawer

#### Building from Source
```bash
# Prerequisites: Android Studio, Java 8+, Android SDK

# Navigate to Android project
cd AndroidApp

# Build the project
./gradlew assembleRelease

# APK will be generated in app/build/outputs/apk/release/
```

## 📖 How to Use

### Step 1: Set Your Capital
Enter your total trading capital in USDT. This is the amount you have available for trading.

### Step 2: Configure Risk
- **Risk Distribution**: Set the percentage of capital you're willing to risk (recommended: 1-3%)
- **Entry Price**: Enter your planned entry price for the trade

### Step 3: Set Risk Levels
Define your stop-loss levels as percentages (e.g., "1,2" for 1% and 2% stop-loss levels).

### Step 4: Configure Take-Profits
Enter your take-profit levels as percentages above entry price (e.g., "3,5,8" for 3%, 5%, and 8% profit targets).

### Step 5: Review Calculations
The calculator will show:
- Position sizes for each risk level
- Potential profits and losses
- Capital impact for each scenario
- Risk-reward ratios

### Step 6: Execute Trade
Use the calculated position sizes to place your trades on your preferred exchange.

## 📁 Project Structure

```
risk-calculator/
├── index.html              # Main web application
├── README.md               # Project documentation
├── AndroidApp/             # Android application source
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/uz/jastechno/riskc/
│   │   │   │   └── MainActivity.kt    # Main Android activity
│   │   │   ├── res/                   # Android resources
│   │   │   └── AndroidManifest.xml    # App manifest
│   │   ├── build.gradle.kts           # App build configuration
│   │   └── release/
│   │       └── app-release.apk        # Built Android app
│   ├── build.gradle.kts               # Project build configuration
│   └── gradle/                        # Gradle wrapper and dependencies
└── .github/                           # GitHub workflows and settings
```

## 🛠 Technology Stack

### Web Application
- **HTML5**: Modern semantic markup
- **CSS3**: Responsive design with CSS Grid and Flexbox
- **Vanilla JavaScript**: Lightweight, no dependencies
- **SVG Export**: Built-in data export functionality

### Android Application
- **Kotlin**: Modern Android development language
- **WebView**: Hybrid app approach using web technologies
- **Android SDK**: Target API 34, minimum API 21
- **Gradle**: Build system and dependency management

### Features
- **Responsive Design**: Mobile-first approach
- **Progressive Enhancement**: Works without JavaScript (basic functionality)
- **Cross-Platform**: Single codebase for web and mobile

## 🤝 Contributing

We welcome contributions! Please feel free to submit issues, feature requests, or pull requests.

### Development Setup
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Test thoroughly (web and Android if applicable)
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

### Code Style
- Use modern JavaScript (ES6+)
- Follow mobile-first responsive design principles
- Keep the design clean and minimal
- Ensure accessibility standards are met

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## ⚠️ Disclaimer

This tool is for educational and informational purposes only. Cryptocurrency trading involves substantial risk of loss. Always do your own research and never trade with money you can't afford to lose. The developers are not responsible for any trading losses.

---

**Built with ❤️ by [JasTechno](https://github.com/jasurbek97)**

*Happy trading! 🚀*
