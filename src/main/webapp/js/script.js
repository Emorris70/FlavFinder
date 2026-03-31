const init = () => {
    console.log("Hello World!!");
    popUp();
}

const popUp = () => {
    const locationBtn = document.getElementById("location-toggle-btn");
    const dropElement = document.getElementById("pop-up");

    locationBtn.addEventListener("click", () => {
        console.log("clicked");
        dropElement.classList.toggle("show");
    });

}
window.addEventListener("DOMContentLoaded", init);