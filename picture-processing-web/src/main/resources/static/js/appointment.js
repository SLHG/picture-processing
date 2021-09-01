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

    function bthClick(bth) {

        let image1 = document.getElementById("image2");
        let image = new Image(200, 200);
        image.src = image1.src;
        console.log(image)
        let context = canvas.getContext("2d");
        context.fillStyle = bth.value;
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.drawImage(image, 0, 0, 200, 200);
        document.querySelector("#image3").setAttribute("src", canvas.toDataURL());
    }
}
