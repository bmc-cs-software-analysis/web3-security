ARG VARIANT="jammy"
FROM mcr.microsoft.com/vscode/devcontainers/base:0-${VARIANT}
RUN chsh -s /usr/bin/zsh vscode
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends python3 python3-pip python3-setuptools curl git nodejs npm
RUN pip3 install solc-select slither-analyzer==0.9.6
RUN curl -L "https://github.com/crytic/echidna/releases/download/v2.0.3/echidna-test-2.0.3-Ubuntu-18.04.tar.gz" \
    | tar -xz -C /usr/bin
RUN curl -L https://github.com/Certora/gambit/releases/download/v1.0.6/gambit-linux-v1.0.6 -o /usr/local/bin/gambit \
    && chmod +x /usr/local/bin/gambit
