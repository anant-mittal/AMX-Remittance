git config --global alias.up \
 '!sh -c '\''git pull --rebase origin "$(git rev-parse --symbolic-full-name HEAD)"'\'' -'

git config --global alias.pu \
 '!sh -c '\''git push origin "$(git rev-parse --symbolic-full-name HEAD)"'\'' -'

