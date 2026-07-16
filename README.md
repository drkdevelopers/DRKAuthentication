# DRK Authentication

A lightweight, secure, and highly configurable authentication plugin for Minecraft servers.

DRK Authentication helps protect offline-mode servers by requiring players to register and log in before they can play. Built for modern Minecraft servers using the Paper API, it focuses on performance, security, and ease of use.

## ✨ Features

* 🔐 Secure player registration and login
* 💾 SQLite database support
* 🔒 Password hashing for enhanced security
* ⚡ Fast and lightweight
* ⚙️ Fully configurable messages
* 🛡️ Blocks player interaction until authenticated
* 🔄 Automatic data loading and saving
* 📁 Clean and organized configuration files
* 🚀 Built for modern Paper servers

## 📦 Commands

| Command                          | Description            |
| -------------------------------- | ---------------------- |
| `/register <password> <confirm>` | Register a new account |
| `/login <password>`              | Log in to your account |

## 🔑 Permissions

| Permission                   | Description                       |
| ---------------------------- | --------------------------------- |
| `drkauthentication.register` | Allows players to register        |
| `drkauthentication.login`    | Allows players to log in          |
| `drkauthentication.admin`    | Access to administrative features |

> Permission nodes may expand as new features are added.

## 📂 Configuration

Configuration files are located in:

```text
plugins/DRKAuthentication/
```

Example files:

* `config.yml`
* `messages.yml`
* `database.db`

## 🖥️ Compatibility

* Minecraft 1.21.x
* Paper
* Java 21

## 📥 Installation

1. Download the latest release.
2. Place the `.jar` file into your server's `plugins` folder.
3. Start or restart your server.
4. Configure the plugin in the generated configuration files.
5. Enjoy secure player authentication.

## 🛠️ Building

Clone the repository:

```bash
git clone https://github.com/drkdevelopers/DRKAuthentication.git
```

Build with Maven:

```bash
mvn clean package
```

The compiled plugin will be generated in:

```text
target/
```

## 📋 Roadmap

* Password reset system
* Session management
* Email verification (optional)
* MySQL support
* PostgreSQL support
* Two-factor authentication
* PlaceholderAPI support
* Multi-language support

## 🤝 Contributing

Contributions, bug reports, and feature suggestions are welcome. Feel free to open an issue or submit a pull request.

## 📄 License

This project is licensed under the MIT License.

---

Developed with ❤️ by **DRK Developers**.
