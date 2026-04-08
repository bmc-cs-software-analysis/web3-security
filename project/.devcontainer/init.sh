#!/bin/bash
set -e

echo "Installing solc-select..."
pip install solc-select

echo "Setting up Solidity 0.8.0..."
solc-select install 0.8.0
solc-select use 0.8.0
echo "Installed Solidity $(solc --version | tail -n1)"

echo "Installing Rust..."
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y
. "$HOME/.cargo/env"
rustup default nightly

echo "Installing Mythril..."
pip install mythril

echo "Installing fzf..."
git clone --depth 1 https://github.com/junegunn/fzf.git ~/fzf && \
    ~/fzf/install --all || true

echo "Setting up shell..."
rm -f ~/.zsh_history
touch .devcontainer/zsh_history
ln -sf "$(pwd)/.devcontainer/zsh_history" ~/.zsh_history

rm -f ~/.zshrc
ln -sf "$(pwd)/.devcontainer/zshrc" ~/.zshrc

echo "Done."
