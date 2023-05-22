# 期末專案
## 待辦事項
- 主要
  - 在側欄顯示所有清單
  - 為任務加入新增時間及所在的清單
  - 增加排序功能 -> 依任務時間、新增時間、名稱 ...?
  - 點擊 radio button 時把 Done 改成 True
  - 新增一個顯示完成任務的清單
  - 從 CreateNewFile 切換回 ChoosingFile 時還原警告訊息
- 可選
  - 深色模式
  - ID 改為 auto increment 以確保唯一性
  - 為任務添加優先級、標籤

## 記錄

<details>
<summary>5/22</summary>

- 對任務點擊左鍵時，任務會變為淺藍色並跳出選單，可以用來編輯或刪除任務
- 將 DatabaseManger 的一些 SQL 語句改為參數化查詢以避免 SQL 注入
- 為任務加入隨機字母和數字組成的 ID，便於刪除或編輯資料庫中的資料

</details>
<details>
<summary>5/21</summary>

- 可以新增任務至資料庫
- 新增用來管理對話框的類別 DialogMangager
  - 在新增任務時跳出，並把輸入的資料新增到資料庫
- 新增用來管理任務列表的類別 TaskManager
  - 建立一個任務並把他加到任務列表上 (UI)
  - 刷新任務列表
- 新增一個環境變數的類別 Globe
  - 把 list 新增至 Globe 以便於在其他類別呼叫刷新列表的功能
- 新增一個用來監聽場景變換的 listener
  - 可以在場景切換到 MainPage 時刷新列表

</details>
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