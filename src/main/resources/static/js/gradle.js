var parsedData = null;
function setData(newData, projectName) {

    var $jstree = $('#jstree');
    var treeData = [];
    var projectList = [];
    var ignoreTests = $("input[name='ignoreTests']").is(":checked");

    $.each(newData.projects, function (title, project) {
        projectList.push("<option>{0}</option>".formatX(title));
    });
    $.each(newData.projects, function (index, project) {
        var show = true;
        if (projectName) {
            if (index != projectName) {
                show = false;
            }
        }
        if (show) {
            $.each(project.sourceSet, function (sourceSetName, sub) {
                if (!(ignoreTests && sourceSetName.toLowerCase().contains('test'))) {
                    $.each(sub, function (index, treeItem) {
                        if (index == 0) {
                            treeItem.text += '<i> (' + sourceSetName + ')</i>';
                        }
                        treeData.push(treeItem);
                    });
                }
            });
            return false;
        }
    });

    if (!projectName) {
        $("#projectList")
            .html(projectList)
            .selectpicker('refresh');
    }

    $jstree.jstree(true).settings.core.data = treeData;
    $jstree.jstree(true).refresh();
}

function doParse(pastedData) {
    $('#gradlePasteArea').val(pastedData);
    var sentDataObject = {
        "data": pastedData
    };
    $.ajax({
        type: "POST",
        url: "/gradle/parse",
        data: JSON.stringify(sentDataObject),
        traditional: true,
        contentType: "application/json",
        fail : function(data) {
            toastr["error"](data, "Failed to parse input data");
        },
        success: function (data) {
            parsedData = data;
            setData(jQuery.extend(true, {}, data), null);
        },
        dataType: "json"
    });
}
function handlePaste(e) {
    var clipboardData, pastedData;

    // Stop data actually being pasted into div
    e.stopPropagation();
    e.preventDefault();

    // Get pasted data via clipboard API
    clipboardData = e.clipboardData || window.clipboardData;
    pastedData = clipboardData.getData('Text');
    // Do whatever with pasteddata
    doParse(pastedData);
}

function getDependencyText(text) {
    text = text.replaceAll("(*)", "");
    var italic = text.indexOf('<i>');
    if (italic >= 0) {
        text = text.substring(0, italic);
    }
    var arrow = text.indexOf('->');
    if (arrow >= 0) {
        text = text.substring(0, arrow);
    }
    return text.trim();
}

function getGroup(text) {
    var index = text.indexOf(":");
    if (index > 0) {
        return text.substring(0, index);
    }
    return text;
}

function getGroupWithArtifact(childNodeText, parentNodeText) {
    var parent = getDependencyText(parentNodeText);
    var child = getDependencyText(childNodeText);

    if (child == parent) {
        return "exclude group: '{0}'".formatX(getGroup(child));
    } else {
        return "compile(\"{0}\"){\n    exclude group: '{1}'\n}".formatX(parent, getGroup(child));
    }
}


function applyData() {
    var val = $('#gradlePasteArea').val();
    if (val != '') {
        doParse(val);
    }
}
function refreshData() {
    if (parsedData) {
        setData(jQuery.extend(true, {}, parsedData), $('#projectList').val());
    }
}

function handleFileSelect(evt) {
    evt.stopPropagation();
    evt.preventDefault();

    var files = evt.dataTransfer.files; // FileList object.

    if (files.length > 0) {
        var fileReader = new FileReader();
        fileReader.onload = function(e) {
            var text = fileReader.result;
             $('#gradlePasteArea').val(text);
            applyData();
        };
        fileReader.readAsText(files[0], "UTF-8")

    }
}

function handleDragOver(evt) {
    evt.stopPropagation();
    evt.preventDefault();
    evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
}

function getSearchTextFromArtifact(text) {
    text = text.replaceAll("(*)", "");
    var italic = text.indexOf('<i>');
    if (italic >= 0) {
        text = text.substring(0, italic);
    }

    var arrowIndex = text.indexOf(" -> ");
    var version = null;
    if (arrowIndex > 0) {
        afterFlag = text.substring(arrowIndex + 4).trim();
        if (!isNaN(afterFlag.charAt(0))) {
            version = afterFlag;
        } else {
            return afterFlag;
        }

        text = text.substring(0, arrowIndex);
        var split = text.split(":");
        if (split.length >= 2) {
            var split2 = split[1];
            var spaceIndex = split2.indexOf(' ');
            if (spaceIndex > 0) {
                split2 = split2.substring(0, spaceIndex);
            }
            if (!version) {
                version = split[2];
            }
            return split2.trim() + ':' + version;
        } else {
            return text + ':' + version;
        }
    } else {
        var split = text.split(":");
        if (split.length >= 2) {
            var split2 = split[1];
            var spaceIndex = split2.indexOf(' ');
            if (spaceIndex > 0) {
                split2 = split2.substring(0, spaceIndex);
            }
            if (!version) {
                version = split[2];
            }
            return split2.trim() + ':' + version;
        } else {
            return text;
        }
    }
}

function searchForArtifact(text) {
    var $searchTree = $('#searchTree');
    $searchTree.val(getSearchTextFromArtifact(text));
    $searchTree.trigger("input");
}


$(function () {
    //document.getElementById('gradlePasteArea').addEventListener('paste', handlePaste);

    // Setup the dnd listeners.
    var dropZone = document.getElementById('gradlePasteArea');
    dropZone.addEventListener('dragover', handleDragOver, false);
    dropZone.addEventListener('drop', handleFileSelect, false);

    $("#menuGradle").addClass("active");
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-bottom-left",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "2000",
        "hideDuration": "3000",
        "timeOut": "3000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };


    var $jstree = $('#jstree');
    $('#clearButton').click(function () {
        $('#gradlePasteArea').val('');
    });

    $('#applyButton').click(function () {
        applyData();
    });


    $('#expandButton').click(function () {
        $jstree.jstree("open_all");
    });

    $('#collapseButton').click(function () {
        $jstree.jstree("close_all");
    });

    $('#projectList').on('hidden.bs.select', function (e) {
        refreshData();
    });

    $("input[name='ignoreTests']").prop("checked", true);
    $('#ignoreTests').change(function () {
        refreshData();
    });

    $jstree.jstree({
        "core": {
            "animation": 0,
            "themes": {"stripes": true}
        },
        "types": {
            "root": {
                "icon": "/static/3.3.3/assets/images/tree_icon.png",
                "valid_children": ["default"]
            },
            "default": {
                "valid_children": ["default", "file"]
            },
            "file": {
                "icon": "glyphicon glyphicon-file",
                "valid_children": []
            }
        },
        "contextmenu":{
            "items": function($node) {
                var tree = $jstree.jstree(true);
                return {
                    "Search-for-artifact": {
                        "separator_before": false,
                        "separator_after": true,
                        "label": "Search for artifact",
                        "action": function (obj) {
                            searchForArtifact(tree.get_text($node));
                        }
                    },
                    "Exclude": {
                        "separator_before": false,
                        "separator_after": false,
                        "label": "Exclude dependency",
                        "action": function (obj) {
                            var path = tree.get_path($node, null, true);
                            var rootNode = tree.get_node(path[0]);
                            copyToClipboard(getGroupWithArtifact(tree.get_text($node), tree.get_text(rootNode)));
                            toastr["info"]("Copied to clipboard", "Exclude dependency");
                        }
                    }
                };
            }
        },
        "search": {
            "case_insensitive": true,
            "show_only_matches": true
        },
        "plugins": [
            "contextmenu","dnd", "search", "wholerow"
        ]
    });
    var to = null;
    var $searchTree = $('#searchTree');
    $jstree.on('refresh.jstree', function () {
        var v = $searchTree.val();

        setTimeout(function () {
            $jstree.jstree(true).search(v);
        }, 50);
    });
    $searchTree.on('input', function () {
        if (to) {
            clearTimeout(to);
        }
        to = setTimeout(function () {
//            var v = $searchTree.val();
            $jstree.jstree(true).refresh();

        }, 500);
    });


});