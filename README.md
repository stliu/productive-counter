也可以换个思路, 不使用callback的方式, 而是

1. 获取当前org下面所有的repos

       
        GET /orgs/:org/repos

2. 获取当前org下面所有的members

        GET /orgs/:org/members
        
3. 获取每个member的Name和头像
        
        "login": "alexgreenbar",
        "id": 2724275,
        "avatar_url": "https://avatars.githubusercontent.com/u/2724275?v=3",
        "url": "https://api.github.com/users/alexgreenbar" //Name可以调用这个API去获取

这两个都是每次都获取一遍(并保存起来?)

然后使用[GET /repos/:owner/:repo/stats/contributors](https://developer.github.com/v3/repos/statistics/#contributors)
来对每个repo都调用一遍

返回的结果中就包括了每个repo中每个人的提交信息了, 包括每个人每周增加修改删除了多少行代码


可以通过 /repos/:owner/:repo/events 来获取到一个repo的所有推送信息


1. 列出当前org下有哪些repo

	记录下每个repo的id/name/创建时间

	注意这个api会通过header中Link的方式来分页

2. 获取每个repo的每天的commit信息

	https://api.github.com/repos/easemob/sdkexamples-ios/commits?since=2015-05-01T00:00:00&until=2015-05-02T00:00:00
	
	这个会返回这一天的所有的commit
	
3. 根据上面返回的每个commit, 再调用https://api.github.com/repos/easemob/sdkexamples-ios/commits/a959c2613131e597489e7b3909de549daa3be082

	这个api会返回每个commit的stat信息, 例如增加删除修改了多少行
	