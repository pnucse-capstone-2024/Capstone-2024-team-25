import { faAward, faRetweet, faFingerprint, faUserFriends } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default function ContactFeatures() {
    return (<section className="pb-20 bg-gray-300 -mt-24">
    <div className="container mx-auto px-4">
      <div className="flex flex-wrap">
        <div className="lg:pt-12 pt-6 w-full md:w-4/12 px-4 text-center">
          <div className="relative flex flex-col min-w-0 break-words bg-white w-full mb-8 shadow-lg rounded-lg">
            <div className="px-4 py-5 flex-auto">
              <div className="text-white p-3 text-center inline-flex items-center justify-center w-12 h-12 mb-5 shadow-lg rounded-full bg-red-400">
                <FontAwesomeIcon icon={faAward} />
              </div>
              <h6 className="text-xl font-semibold">Awarded Agency</h6>
              <p className="mt-2 mb-4 text-gray-600">
                Divide details about your product or agency work into parts. A paragraph describing a feature will be enough.
              </p>
            </div>
          </div>
        </div>

        <div className="w-full md:w-4/12 px-4 text-center">
          <div className="relative flex flex-col min-w-0 break-words bg-white w-full mb-8 shadow-lg rounded-lg">
            <div className="px-4 py-5 flex-auto">
              <div className="text-white p-3 text-center inline-flex items-center justify-center w-12 h-12 mb-5 shadow-lg rounded-full bg-blue-400">
                <FontAwesomeIcon icon={faRetweet} />
              </div>
              <h6 className="text-xl font-semibold">
                Free Revisions
              </h6>
              <p className="mt-2 mb-4 text-gray-600">
                Keep you user engaged by providing meaningful information. Remember that by this time, the user is curious.
              </p>
            </div>
          </div>
        </div>

        <div className="pt-6 w-full md:w-4/12 px-4 text-center">
          <div className="relative flex flex-col min-w-0 break-words bg-white w-full mb-8 shadow-lg rounded-lg">
            <div className="px-4 py-5 flex-auto">
              <div className="text-white p-3 text-center inline-flex items-center justify-center w-12 h-12 mb-5 shadow-lg rounded-full bg-green-400">
                <FontAwesomeIcon icon={faFingerprint} />
              </div>
              <h6 className="text-xl font-semibold">
                Verified Company
              </h6>
              <p className="mt-2 mb-4 text-gray-600">
                Write a few lines about each one. A paragraph describing a feature will be enough. Keep you user engaged!
              </p>
            </div>
          </div>
        </div>
      </div>


      <div className="flex flex-wrap items-center mt-32">
        <div className="w-full md:w-5/12 px-4 mr-auto ml-auto">
          <div className="text-gray-600 p-3 text-center inline-flex items-center justify-center w-16 h-16 mb-6 shadow-lg rounded-full bg-gray-100">
            <FontAwesomeIcon icon={faUserFriends} className="text-xl"/>
          </div>
          <h3 className="text-3xl mb-2 font-semibold leading-normal">
            Working with us is a pleasure
          </h3>
          <p className="text-lg font-light leading-relaxed mt-4 mb-4 text-gray-700">
            Dont let your uses guess by attaching tooltips and popoves
            to any element. Just make sure you enable them first via
            JavaScript.
          </p>
          <p className="text-lg font-light leading-relaxed mt-0 mb-4 text-gray-700">
            The kit comes with three pre-built pages to help you get
            started faster. You can change the text and images and
            youre good to go. Just make sure you enable them first via
            JavaScript.
          </p>
          <a
            href="/"
            className="font-bold text-gray-800 mt-8"
          >
            Check Tailwind Starter Kit!
          </a>
        </div>

        <div className="w-full md:w-4/12 px-4 mr-auto ml-auto">
          <div className="relative flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow-lg rounded-lg bg-pink-600">
            <img
              alt="..."
              src="https://images.unsplash.com/photo-1522202176988-66273c2fd55f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1051&q=80"
              className="w-full align-middle rounded-t-lg"
            />
            <blockquote className="relative p-8 mb-4">
              <svg
                preserveAspectRatio="none"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 583 95"
                className="absolute left-0 w-full block"
                style={{
                  height: "95px",
                  top: "-94px"
                }}
              >
                <polygon
                  points="-30,95 583,95 583,65"
                  className="text-pink-600 fill-current"
                 />
              </svg>
              <h4 className="text-xl font-bold text-black">
                Top Notch Services
              </h4>
              <p className="text-md font-light mt-2 text-gray-700">
                The Arctic Ocean freezes every winter and much of the
                sea-ice then thaws every summer, and that process will
                continue whatever happens.
              </p>
            </blockquote>
          </div>
        </div>

      </div>
    </div>
  </section>);
}