<!DOCTYPE html>
<html>
<head>
<style>
.wheel {
    width: 300px;
    height: 300px;
    border: 10px solid black;
    border-radius: 50%;
    position: relative;
    overflow: hidden;
}

.segment {
    width: 50%;
    height: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform-origin: 0 0;
}

.red {
    background-color: red;
}

.black {
    background-color: black;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

.spinning {
    animation: spin 3s ease-out;
}
</style>
</head>
<body>
<div class="wheel" id="wheel">
    <!-- Создайте секторы -->
    <div class="segment red" style="transform: rotate(0deg);"></div>
    <div class="segment black" style="transform: rotate(45deg);"></div>
    <div class="segment red" style="transform: rotate(90deg);"></div>
    <div class="segment black" style="transform: rotate(135deg);"></div>
    <div class="segment red" style="transform: rotate(180deg);"></div>
    <div class="segment black" style="transform: rotate(225deg);"></div>
    <div class="segment red" style="transform: rotate(270deg);"></div>
    <div class="segment black" style="transform: rotate(315deg);"></div>
</div>

<button onclick="spinWheel()">Крутить колесо</button>

<script>
    function spinWheel() {
    const wheel = document.getElementById('wheel');
    wheel.classList.add('spinning');

    setTimeout(() => {
    wheel.classList.remove('spinning');
    // Можно добавить логику остановки на определенном секторе
}, 3000);
}
</script>
</body>
</html>
