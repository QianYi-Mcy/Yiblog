/* 主题切换：优先本地记忆，其次后端默认值 */
(function () {
    var root = document.documentElement;

    function normalize(theme) {
        return theme === 'red' ? 'red' : 'green';
    }

    function apply(theme) {
        theme = normalize(theme);
        if (theme === 'red') {
            root.setAttribute('data-theme', 'red');
        } else {
            root.removeAttribute('data-theme');
        }
        var label = document.getElementById('themeLabel');
        if (label) {
            label.textContent = theme === 'red' ? '红色主题' : '绿色主题';
        }
    }

    // 后端注入的默认主题（页面通过 data-default-theme 属性传入）
    var serverDefault = normalize(root.getAttribute('data-default-theme') || 'green');
    var saved = localStorage.getItem('blog-theme');
    apply(saved ? saved : serverDefault);

    document.addEventListener('DOMContentLoaded', function () {
        var toggle = document.getElementById('themeToggle');
        if (toggle) {
            toggle.addEventListener('click', function () {
                var current = root.getAttribute('data-theme') === 'red' ? 'red' : 'green';
                var next = current === 'red' ? 'green' : 'red';
                apply(next);
                localStorage.setItem('blog-theme', next);
            });
        }
    });
})();
