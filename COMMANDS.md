# GIT Commands

## Basic Commands
```
$ git add <filename>
$ git commit -m "<message>"
$ git push origin <branch>  //Push to branch

// if not able to push
$ git pull --rebase origin master

```

## Multi-branch setup
```
$ git checkout <branch> // Switch to Existing Branch
$ git checkout -b <branch> //Create New Branch
$ git tag <label>
```


## Advanced 
```
$ git reset --soft HEAD~ 
$ git reset --hard HEAD   ///ABORT merge
$ git commit --amend -am  “TOL-135: css improvement”

//Delete Remote commit -  https://ncona.com/2011/07/how-to-delete-a-commit-in-git-local-and-remote/
$ git push origin +master

Sync with Remote 
$ git fetch --prune

$ git reset --hard HEAD
$ git clean -f -x -d -n
```


## Create shortcuts
```
$ git config --global alias.up \
 '!sh -c '\''git pull --rebase origin "$(git rev-parse --symbolic-full-name HEAD)"'\'' -'

$ git config --global alias.pu \
 '!sh -c '\''git push origin "$(git rev-parse --symbolic-full-name HEAD)"'\'' -'

// Delete branch
$ git branch -d branch_name
// Remotely
$ git push origin --delete branch_name
```


