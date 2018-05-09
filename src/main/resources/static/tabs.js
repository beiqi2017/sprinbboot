/**
  * 增加标签页
  */
 function addTab(tabName,tabUrl,tabTitle) {
	 
     var exists = checkTabIsExists(tabName);
     if(exists){
         $("#tab_a_"+tabName).click();
     } else {
         $("#tablist").append('<li id="tab_li_'+tabName+'"><a href="#tab_content_'+tabName+'" data-toggle="tab" id="tab_a_'+tabName+'">'+tabTitle+'<span class="glyphicon glyphicon-remove" onclick="closeTab(this);"></span></a></li>');
         iframeHeight =$("#home").outerHeight();
         var id="iframe_"+tabName;
         var content = '<iframe id="'+id+'" src="' +tabUrl+"?id="+id+ '" style="width:100%;margin: 0;padding: 0;" frameborder="0" scrolling="no" onload="javascript:iframeAutoHeight(this.id);" ></iframe>';
         $("#tab-content").append('<div id="tab_content_'+tabName+'" style="height: '+iframeHeight+'px;overflow: auto;" role="tabpanel" class="tab-pane">'+content+'</div>');
         $("#tab_a_"+tabName).click();
     }
 }
  
  
 /**
  * 关闭标签页
  * @param button
  */
 function closeTab (button) {
      
     //通过该button找到对应li标签的id
     var li_id = $(button).parent().parent().attr('id');
     var id = li_id.replace("tab_li_","");
      
     //如果关闭的是当前激活的TAB，激活他的前一个TAB
     if ($("li.active").attr('id') == li_id) {
         $("li.active").prev().find("a").click();
     }
      
     //关闭TAB
     $("#" + li_id).remove();
     $("#tab_content_" + id).remove();
 };
  
 /**
  * 判断是否存在指定的标签页
  * @param tabMainName
  * @param tabName
  * @returns {Boolean}
  */
 function checkTabIsExists(tabName){
     var tab = $("#tablist > #tab_li_"+tabName);
     //console.log(tab.length)
     return tab.length > 0;
 }
 
 
 function iframeAutoHeight(id) {
     var iframeObj = document.getElementById(id);
     if (iframeObj) {
         if (iframeObj.contentDocument && iframeObj.contentDocument.body.offsetHeight) {
             iframeObj.height = iframeObj.contentDocument.body.offsetHeight+10;
         } else if (iframeObj.Document && iframeObj.Document.body.scrollHeight) {
             iframeObj.height = iframeObj.Document.body.scrollHeight;
         }
     }
 }