<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>示例页面</title>
    <link rel="stylesheet" type="text/css" href="statics/ui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="statics/ui/themes/icon.css">
    <script type="text/javascript" src="statics/ui/jquery.min.js"></script>
    <script type="text/javascript" src="statics/ui/jquery.easyui.min.js"></script>
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
    </style>
</head>
<body class="easyui-layout">
<div data-options="region:'north',border:false" style="height:60px;background:rgba(215, 223, 218, 0.85);padding:10px">头部部分</div>
<div data-options="region:'west',split:true,title:'菜单'" style="width:150px;padding:10px;">菜单部分</div>
<div data-options="region:'south',border:false" style="height:40px;background:#A9FACD;padding:10px;">页尾部分</div>
<div data-options="region:'center',title:'内容'" style="margin-right: 3px;">
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
            <th data-options="field:'listprice',align:'right',editor:'textbox',editor:{type:'numberbox',options:{precision:1}}" width="10%">总账科目</th>
            <th data-options="field:'unitcost',align:'right',editor:'textbox'" width="10%">明细科目</th>
            <th data-options="field:'item',editor:'textbox'" width="10%">客户/项目</th>
            <th data-options="field:'borrowMon',align:'center',editor:{type:'numberbox',options:{precision:1}}" width="10%">借方金额</th>
            <th data-options="field:'lendMoney',align:'center',editor:{type:'numberbox',options:{precision:1}}" width="10%">贷方金额</th>
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
</body>
<script type="text/javascript">
    var editIndex = undefined;
    function endEditing() {
        if (editIndex == undefined) {
            return true
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
