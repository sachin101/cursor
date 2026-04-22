## Server setup (Linux + systemd)

### 1) Create user and directory

```bash
sudo useradd --system --create-home --shell /usr/sbin/nologin javaapp || true
sudo mkdir -p /opt/java-program
sudo chown -R javaapp:javaapp /opt/java-program
```

### 2) Install Java 21

Use your distro’s packages (Temurin/OpenJDK 21).

### 3) Install systemd unit

Copy `deploy/java-program.service` to:

```bash
sudo cp java-program.service /etc/systemd/system/java-program.service
sudo systemctl daemon-reload
sudo systemctl enable --now java-program
sudo systemctl status java-program --no-pager
```

### 4) Open firewall / reverse proxy

App listens on `:8080` by default (`application.yaml`). Put Nginx/Apache in front if needed.

