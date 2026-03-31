const init = () => {
console.log("Hello World!!");
dropDown();
}

const dropDown = () => {
    const locationBtn = document.getElementById("location-toggle-btn");
    const dropElement = document.getElementById("drop-down")

    locationBtn.addEventListener("click", () => {
        dropElement.classList.add("show");
    });


}
window.addEventListener("DOMContentLoaded", init);