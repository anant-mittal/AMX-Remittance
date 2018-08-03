GIT Commands

git reset --soft HEAD~ 
git reset --hard HEAD   ///ABORT merge
git commit --amend -am  “TOL-135: css improvement”
git commit -m "SNAPDEALTECH-74188 - ui-"
git commit -m "TOL-135:CodeReview n GruntCheck”
git commit -m "TOL-135:ReadME”
git commit -m "SNAPDEALTECH-75046 - ui-"

Delete Remote commit -  https://ncona.com/2011/07/how-to-delete-a-commit-in-git-local-and-remote/
git push origin +master

Sync with Remote 
git fetch --prune

git reset --hard HEAD
git clean -f -x -d -n

git config --global alias.up \
 '!sh -c '\''git pull --rebase origin "$(git rev-parse --symbolic-full-name HEAD)"'\'' -'

git config --global alias.pu \
 '!sh -c '\''git push origin "$(git rev-parse --symbolic-full-name HEAD)"'\'' -'

Delete branch
git branch -d branch_name
Remotely
git push origin --delete branch_name

