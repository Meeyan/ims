<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>后台管理系统</title>
    <script type="text/javascript" src="statics/ui/jquery.min.js"></script>

    <style>
        body {
            font-family: verdana, helvetica, arial, sans-serif;
            padding: 20px;
            font-size: 12px;
            margin: 0;
        }

        h2 {
            font-size: 18px;
            font-weight: bold;
            margin: 0;
            margin-bottom: 15px;
        }

        .demo-info {
            padding: 0 0 12px 0;
        }

        .demo-tip {
            display: none;
        }

        .label-top {
            display: block;
            height: 22px;
            line-height: 22px;
            vertical-align: middle;
        }

        .easyui-tree > li div {
            padding: 5px;
            height: 16px;
            line-height: 16px;
        }

        .easyui-tree > li > div span.tree-title {
            font-size: 12px;
            padding-left: 5px;
        }
    </style>
</head>
<body class="easyui-layout">
<div class="wu-header" data-options="region:'north',border:false,split:true">
    <div class="wu-header-left">
        <h1>EasyUI Web Admin</h1>
    </div>
    <div class="wu-header-right">
        <p><strong class="easyui-tooltip" title="2条未读消息">admin</strong>，欢迎您！</p>
        <p><a href="#">网站首页</a>|<a href="#">支持论坛</a>|<a href="#">帮助中心</a>|<a href="#">安全退出</a></p>
    </div>
</div>

<div class="wu-sidebar" data-options="region:'west',split:true,border:true,title:'导航菜单'">
    <div class="easyui-accordion" id="leftMenus" data-options="border:false,fit:true"></div>
</div>

<div class="wu-main" data-options="region:'center'">
    <div id="wu-tabs" class="easyui-tabs" data-options="border:false,fit:true">
        <div title="首页" data-options="closable:false,iconCls:'icon-tip',cls:'pd3'">
            <table class="easyui-datagrid" id="dg" title="明细项目" style="height:auto;"
                   data-options="singleSelect:true,collapsible:true,url:'jsonData',method:'get',onClickCell: onClickCell,onEndEdit: onEndEdit">
                <thead>
                <tr>
                    <th data-options="field:'itemid',editor:'textbox'" width="5%">序号</th>
                    <th data-options="field:'productid',
                width:100,
                formatter:function(value,row){
                    return row.productname;
                },
                editor:{
                    type:'combobox',
                    options:{
                        valueField:'productid',
                        textField:'productname',
                        url:'getDataPro',
                        required:true
                    }
                }" width="20%">摘要
                    </th>
                    <th data-options="field:'listprice',align:'right',editor:'textbox',editor:{type:'numberbox',options:{precision:1}}" width="15%">总账科目</th>
                    <th data-options="field:'unitcost',align:'right',editor:'textbox'" width="15%">明细科目</th>
                    <th data-options="field:'item',editor:'textbox'" width="15%">客户/项目</th>
                    <th data-options="field:'borrowMon',align:'center',editor:{type:'numberbox',options:{precision:1}}" width="15%">借方金额</th>
                    <th data-options="field:'lendMoney',align:'center',editor:{type:'numberbox',options:{precision:1}}" width="15%">贷方金额</th>
                </tr>
                </thead>
            </table>
            <div id="tb" style="height:auto">
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="append()">添加项目</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除项目</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">保存</a>
                <%--<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="reject()">Reject</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="getChanges()">GetChanges</a>--%>
            </div>
        </div>
    </div>
</div>

<!-- 页脚-开始 -->
<div class="wu-footer" data-options="region:'south',border:true,split:true">
    &copy; 2017 UI All Rights Reserved
</div>
<!-- 页脚-结束 -->

</body>

<script type="text/javascript" src="statics/ui/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="statics/ui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="statics/ui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="statics/css/wu.css"/>
<link rel="stylesheet" type="text/css" href="statics/css/icon.css"/>

<script>
    $(document).ready(function () {
        $.ajax({
            url: 'data/menus_2.json',
            cache: true,
            success: function (data) {
                $.each(data, function (i, topMenu) {
                    var menuList = '<ul class="easyui-tree wu-side-tree">';
                    // 子菜单
                    $.each(topMenu.children, function (j, secMenu) {
                        var iframe = 0;
                        if (secMenu.url.indexOf('http') >= 0) {
                            iframe = 1;
                        }
                    });
                    menuList += '</ul>';
                    // 先初始化父菜单
                    $('#leftMenus').accordion('add', {
                        title: topMenu.text,
                        id: 'topM_' + topMenu.id,
                        content: menuList,
                        iconCls: 'icon ' + topMenu.iconCls,
                        selected: topMenu.state
                    });

                    // 树形菜单
                    $('#topM_' + topMenu.id).tree({
                        data: topMenu.children,
                        onClick: function (node) {
                            if (node.length == 0) {
                                return;
                            }
                            // 添加选项卡
                            addTab(node.text, node.url, node.iconCls, 0);
                        }
                    });
                });
            }
        });
    });

    /**
     * Name 添加菜单选项
     * Param title 名称
     * Param href 链接
     * Param iconCls 图标样式
     * Param iframe 链接跳转方式（true为iframe，false为href）
     */
    function addTab(title, href, iconCls, iframe) {
        var tabPanel = $('#wu-tabs');
        if (!tabPanel.tabs('exists', title)) {
            var content = '<iframe scrolling="auto" frameborder="0"  src="' + href + '" style="width:100%;height:100%;"></iframe>';
            if (iframe) {
                tabPanel.tabs('add', {
                    title: title,
                    content: content,
                    iconCls: iconCls,
                    fit: true,
                    cls: 'pd3',
                    closable: true
                });
            } else {
                tabPanel.tabs('add', {
                    title: title,
                    href: href,
                    iconCls: iconCls,
                    fit: true,
                    cls: 'pd3',
                    closable: true
                });
            }
        }
        else {
            tabPanel.tabs('select', title);
        }
    }


    var editIndex = undefined;
    function endEditing() {
        if (editIndex == undefined) {
            return true;
        }
        if ($('#dg').datagrid('validateRow', editIndex)) {
            $('#dg').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
        } else {
            return false;
        }
    }
    function onClickCell(index, field) {
        if (editIndex != index) {
            if (endEditing()) {
                $('#dg').datagrid('selectRow', index).datagrid('beginEdit', index);
                var ed = $('#dg').datagrid('getEditor', {index: index, field: field});
                if (ed) {
                    ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                }
                editIndex = index;
            } else {
                setTimeout(function () {
                    $('#dg').datagrid('selectRow', editIndex);
                }, 0);
            }
        }
    }
    function onEndEdit(index, row) {
        var ed = $(this).datagrid('getEditor', {
            index: index,
            field: 'productid'
        });
        row.productname = $(ed.target).combobox('getText');
    }
    function append() {
        if (endEditing()) {
            $('#dg').datagrid('appendRow', {status: 'P'});
            editIndex = $('#dg').datagrid('getRows').length - 1;
            $('#dg').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
        }
    }
    function removeit() {
        if (editIndex == undefined) {
            return
        }
        $('#dg').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }
    function accept() {
        if (endEditing()) {
            $('#dg').datagrid('acceptChanges');
        }
    }
    function reject() {
        $('#dg').datagrid('rejectChanges');
        editIndex = undefined;
    }
    function getChanges() {
        var rows = $('#dg').datagrid('getChanges');
        alert(rows.length + ' rows are changed!');
    }

</script>
</html>
