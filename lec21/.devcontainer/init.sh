echo "Installing OpenZeppelin..."
cd /workspaces/web3-security/lec21 && \
npm init -y && \
npm install @openzeppelin/contracts@3.4.2 --save-exact || exit 1

curl "Installing solc"
solc-select install 0.6.0 && solc-select use 0.6.0 || exit 1

echo "Installing fzf..."
git clone --depth 1 https://github.com/junegunn/fzf.git ~/fzf && \
~/fzf/install --all || :

echo "Setting up Shell..."
rm -f ~/.zsh_history && \
touch .devcontainer/zsh_history && \
ln -rs .devcontainer/zsh_history ~/.zsh_history

rm -f ~/.zshrc && \
ln -rs .devcontainer/zshrc ~/.zshrc
