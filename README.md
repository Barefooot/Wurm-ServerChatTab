# ServerChatTab

This mod will create tabs in the chat window where content defined in these properties will appear.

**enabled** - false - mod disabled and will not add tabs [default=false]

**debug** - Toggle logging info for server [default=false]

**tabsCount** - how many tabs to add [default=0 - none]


--- Tabs --- 

--- Tab-1 definition --- 

**tabName.x** - empty means disabled and will not add tab. [default=, means disabled]

**tabLines.x** - use '||' to split lines, can '||||' to have empty-separating lines also [default=""]

e.g. tabLines.1 = Welcome, here are the server rules:||||Rule-1: ...||Rule-2: ...

**tabColor.x** - hex RGB color, can easy select one from here: https://en.wikipedia.org/wiki/Web_colors#Extended_colors , [default=FFFFFF, means white]


--- Tab-2 definition ---

tabName.2 = Subject-1

tabLines.2 = Welcome to Subject-1.

tabColor.2 = 228B22

--- Tab-3 definition ---

tabName.3 =

tabLines.3 = This tab wont show


.. supports [1..tabsCount] tabs
