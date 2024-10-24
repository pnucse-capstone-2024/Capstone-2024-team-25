import Container from "./container";

function Video({ videoId }) {
  return (
    <Container>
      <div className="relative max-w-4xl mx-auto overflow-hidden lg:mb-5 rounded-2xl" style={{ paddingBottom: "55%", paddingLeft: "35%", paddingRight: "35%" }}>
        <iframe
          className="absolute top-0 left-0 w-full h-full"
          src={`https://www.youtube.com/embed/${videoId}`}
          title="YouTube video player"
          frameBorder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
         />
      </div>
    </Container>
  );
}

export default Video;