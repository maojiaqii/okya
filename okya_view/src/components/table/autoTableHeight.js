//获取浏览器窗口高度,处理Element的Table组件的高度问题（height只能是数字或者字符串）
function autoTableHeight() {
  var winHeight = 0;
  if (window.innerHeight) {
    winHeight = window.innerHeight;
  } else if (document.body && document.body.clientHeight) {
    winHeight = document.body.clientHeight;
  } //通过深入Document内部对body进行检测，获取浏览器窗口高度
  if (document.documentElement && document.documentElement.clientHeight) {
    winHeight = document.documentElement.clientHeight;
  }
  // 260  是顶部和底部导航以及部分自定义布局  ；相当于用js实现了 height: calc(100vh - 260px); 的效果
  return winHeight;
}

//浏览器窗口变化时
window.onresize = function() {
  autoTableHeight();
};

//浏览器重新加载时
window.onload = function() {
  autoTableHeight();
};

export default autoTableHeight;
