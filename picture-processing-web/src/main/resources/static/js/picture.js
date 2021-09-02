{
    let file = document.getElementById("file");
    let result;
    file.addEventListener('change', (event) => {
        let files = event.target.files;
        let data = new FormData();
        data.append("file", files[0]);
        let reader = new FileReader()
        // 图片读取成功回调函数
        reader.onload = function (e) {
            document.getElementById('image1').src = e.target.result
        }
        reader.readAsDataURL(files[0])
        $.ajax({
            url: "/service/photo/getForeground",
            type: "POST",
            processData: false,
            contentType: false,
            data: data,
            success: function (d) {
                result = d.result;
                document.getElementById('image2').src = "data:image/png;base64, " + result;
            }
        });
    });
    let canvas = document.createElement('canvas');
    canvas.width = 200;
    canvas.height = 200;
    let context = canvas.getContext("2d");
    let image2 = document.getElementById("image2");
    let x = 0;
    let y = 0;
    let dw = 200;
    let dh = 200;

    function bthClick(bth) {
        context.fillStyle = bth.value;
        context.fillRect(0, 0, canvas.width, canvas.height);
        drawImage();
    }

    function drawImage() {
        let image = new Image(dw, dh);
        image.src = image2.src;
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.drawImage(image, x, y, dw, dh);
        document.querySelector("#image3").setAttribute("src", canvas.toDataURL());
    }

    let mvTid;
    let chgTid;

    function movePhoto(a, b) {
        x = x + a;
        y = y + b;
        drawImage();
    }

    function btnMove(a, b) {
        mvTid = setInterval(function () {
            movePhoto(a, b)
        }, 50)
    }

    function btnUp() {
        clearInterval(mvTid);
    }

    function btnPhotoChange(a) {
        chgTid = setInterval(function () {
            dw = dw + a;
            dh = dh + a;
            drawImage();
        }, 50)
    }

    function ChgBtnUp() {
        clearInterval(chgTid);
    }
}
