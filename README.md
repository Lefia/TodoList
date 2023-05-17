# 期末專案
## 待辦事項
- 場景
  - Main
    - 側欄
    - 任務
    - 新增、移除、更改任務
    - 排序
  - ChoosingFile, CreateNewFile
    - 增加切換場景的 listener 以解決警告訊息還存在的問題

## 記錄
<details>
<summary>5/17</summary>

- 成功連接資料庫
- 修正新增檔案時路徑與名字間沒有反斜線的問題
- Class 可以用 static block 來進行初始化
- 將 ScreenController 改寫為 SceneManager，直接切換 Scene 而不是 root pane

</details>

<details>
<summary>5/11</summary>

- 學到了單例模式，重新改寫了 ScreenController 和 DatabaseController
- 重新改寫 ChoosingFile 介面並新增了 CreateNewFile 
- 開啟檔案時可以判斷使否為 database (regex)
- 可以將選擇的檔案路徑傳到 DatabaseController 中
> 不能以 location 作為 Label 的 fx:id (保留字)
</details>

<details>
<summary>5/10</summary>

- 加入 ScreenController，方便再各個 Controller 裡面切換視窗
- 加入 ChoosingFile 介面，讓使用者能選擇或新建一個新的資料庫
- 將檔案以 package 分類
- 不知道如何在不同檔案共用同一物件
> `getResource("/projectName/file)` 可以從專案輸出的根目錄開始尋找檔案
</details>

<details>
<summary>5/9</summary>

- 排出主要介面 (Main) 的大致格局
</details>