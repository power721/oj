var PlaceHolder = {
    _support: (function() {
        return 'placeholder' in document.createElement('input');
    })(),
 
    // 提示文字的样式，需要在页面中其他位置定义
    className: 'input-placeholder',
    init: function() {
        if (!PlaceHolder._support) {
            // 未对textarea处理，需要的自己加上
            var inputs = document.getElementsByTagName('input');
            PlaceHolder.create(inputs);
        }
    },
 
    create: function(inputs) {
        var input;
        if (!inputs.length) {
            inputs = [inputs];
        }
 
        for (var i = 0, length = inputs.length; i < length; i++) {
            input = inputs[i];
            if (!PlaceHolder._support && input.attributes && input.attributes.placeholder) {
                PlaceHolder._setValue(input);
                input.addEventListener('focus', function(e) {
                    if (this.value === this.attributes.placeholder.nodeValue) {
                        this.value = '';
                        this.className = '';
                    }
                }, false);
 
                input.addEventListener('blur', function(e) {
                    if (this.value === '') {
                        PlaceHolder._setValue(this);
                    }
                }, false);
            }
        }
    },
 
    _setValue: function(input) {
        input.value = input.attributes.placeholder.nodeValue;
        input.className = PlaceHolder.className;
    }
};
 
// 页面初始化时对所有input做初始化
PlaceHolder.init();
